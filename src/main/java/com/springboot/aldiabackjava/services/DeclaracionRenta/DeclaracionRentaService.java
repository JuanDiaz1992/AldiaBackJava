package com.springboot.aldiabackjava.services.DeclaracionRenta;

import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import com.springboot.aldiabackjava.models.heritages.Heritages;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories.IIncomeRepository;
import com.springboot.aldiabackjava.repositories.heritagesRepositories.IHEritageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeclaracionRentaService {

    // Topes DIAN para declaración de renta 2025
    private static final double TOPE_INGRESOS_ANUALES = 65_891_000;
    private static final double TOPE_PATRIMONIO = 211_792_500;
    private static final double TOPE_CONSUMOS = 65_891_000;
    private static final double PORCENTAJE_ASALARIADO = 0.8;

    private final NumberFormat numberFormat = DecimalFormat.getNumberInstance(new Locale("es", "CO"));

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private IIncomeRepository incomeRepository;

    @Autowired
    private IHEritageRepository heritageRepository;

    public ResponseEntity<Map<String, Object>> verificarDeclaracionRenta2025() {
        User user = jwtInterceptor.getCurrentUser();
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Obtener datos
            List<Income> ingresos2024 = incomeRepository.findByUserIdAndYear(user.getIdUser(), 2024);
            List<Heritages> patrimonios = heritageRepository.findByUserIdUser(user.getIdUser())
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());

            // 2. Calcular totales
            double ingresosTotales = calcularIngresosAnuales(ingresos2024);
            double patrimonioTotal = calcularPatrimonioTotal(patrimonios);

            // 3. Verificar topes
            boolean debeDeclararPorIngresos = ingresosTotales >= TOPE_INGRESOS_ANUALES;
            boolean debeDeclararPorPatrimonio = patrimonioTotal >= TOPE_PATRIMONIO;

            // 4. Preparar respuesta con formato
            response.put("debeDeclarar", debeDeclararPorIngresos || debeDeclararPorPatrimonio);
            response.put("motivos", construirMotivosConDetalle(debeDeclararPorIngresos, debeDeclararPorPatrimonio, ingresosTotales, patrimonioTotal));
            response.put("ingresosTotales2024", formatearNumero(ingresosTotales));
            response.put("patrimonioTotal", formatearNumero(patrimonioTotal));
            response.put("topes", construirTopesFormateados());
            response.put("detallePatrimonios", construirDetallePatrimonios(patrimonios));
            response.put("status", 200);

            // Logs con formato
            System.out.println("Ingresos totales: " + formatearNumero(ingresosTotales));
            System.out.println("Patrimonio total: " + formatearNumero(patrimonioTotal));

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            response.put("error", "Error al calcular la declaración de renta: " + e.getMessage());
            response.put("status", 500);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Métodos auxiliares para formateo de números
    private String formatearNumero(double valor) {
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(valor);
    }

    private Map<String, Object> construirMotivosConDetalle(boolean porIngresos, boolean porPatrimonio,
                                                           double ingresosTotales, double patrimonioTotal) {
        Map<String, Object> motivos = new HashMap<>();

        motivos.put("ingresos", crearDetalleMotivo(
                porIngresos,
                ingresosTotales,
                TOPE_INGRESOS_ANUALES,
                "ingresos anuales"
        ));

        motivos.put("patrimonio", crearDetalleMotivo(
                porPatrimonio,
                patrimonioTotal,
                TOPE_PATRIMONIO,
                "patrimonio"
        ));

        return motivos;
    }

    private Map<String, Object> crearDetalleMotivo(boolean supera, double valor, double tope, String concepto) {
        Map<String, Object> detalle = new HashMap<>();
        detalle.put("supera", supera);
        detalle.put("valor", formatearNumero(valor));
        detalle.put("tope", formatearNumero(tope));
        detalle.put("diferencia", formatearNumero(Math.abs(valor - tope)));
        detalle.put("mensaje", supera ?
                String.format("Supera el tope de %s ($%,.0f)", concepto, tope) :
                String.format("No supera el tope de %s ($%,.0f)", concepto, tope));
        return detalle;
    }

    private Map<String, String> construirTopesFormateados() {
        Map<String, String> topes = new HashMap<>();
        topes.put("ingresosAnuales", formatearNumero(TOPE_INGRESOS_ANUALES));
        topes.put("patrimonio", formatearNumero(TOPE_PATRIMONIO));
        topes.put("consumos", formatearNumero(TOPE_CONSUMOS));
        topes.put("mensualEquivalente", formatearNumero(TOPE_INGRESOS_ANUALES / 12));
        return topes;
    }

    private double calcularIngresosAnuales(List<Income> ingresos) {
        return ingresos.stream().mapToDouble(Income::getAmount).sum();
    }

    private double calcularPatrimonioTotal(List<Heritages> patrimonios) {
        return patrimonios.stream().mapToDouble(h -> (double) h.getCurrenValue()).sum();
    }

    private List<Map<String, Object>> construirDetallePatrimonios(List<Heritages> patrimonios) {
        return patrimonios.stream()
                .map(h -> {
                    Map<String, Object> detalle = new HashMap<>();
                    detalle.put("descripcion", h.getDescription());
                    detalle.put("valor", formatearNumero(h.getCurrenValue()));
                    detalle.put("tipo", h.getTypeHeritages() != null ? h.getTypeHeritages().getName() : "Sin tipo");
                    detalle.put("fechaAdquisicion", h.getAcquisitionDate());
                    return detalle;
                })
                .collect(Collectors.toList());
    }

    private boolean esAsalariado(List<Income> ingresos) {
        return true;
    }
}