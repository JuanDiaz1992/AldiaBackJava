package com.springboot.aldiabackjava.services.DeclaracionRenta;

import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories.IIncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeclaracionRentaService {

    // Topes DIAN para declaración de renta 2025 (año gravable 2024)
    private static final double TOPE_INGRESOS_ANUALES = 65_891_000; // $65.891.000
    private static final double TOPE_PATRIMONIO = 211_792_500; // $211.792.500
    private static final double TOPE_CONSUMOS = 65_891_000; // $65.891.000
    private static final double PORCENTAJE_ASALARIADO = 0.8; // 80% de ingresos laborales

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private IIncomeRepository incomeRepository;

    public ResponseEntity<Map<String, Object>> verificarDeclaracionRenta2025() {
        User user = jwtInterceptor.getCurrentUser();
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Obtener todos los ingresos del usuario para el año 2024
            List<Income> ingresos2024 = incomeRepository.findByUserIdAndYear(user.getIdUser(), 2024);

            // 2. Calcular ingresos totales anuales
            double ingresosTotales = calcularIngresosAnuales(ingresos2024);

            // 3. Verificar si supera el tope de ingresos
            boolean debeDeclararPorIngresos = ingresosTotales >= TOPE_INGRESOS_ANUALES;

            // 4. Calcular ingresos mensuales promedio
            double promedioMensual = ingresosTotales / 12;

            // 5. Determinar si debe declarar
            boolean debeDeclarar = debeDeclararPorIngresos;

            // 6. Preparar respuesta
            response.put("debeDeclarar", debeDeclarar);
            response.put("motivo", debeDeclararPorIngresos ?
                    "Supera el tope de ingresos anuales (" + TOPE_INGRESOS_ANUALES + ")" :
                    "No supera los topes mínimos");
            response.put("ingresosTotales2024", ingresosTotales);
            response.put("promedioMensual", promedioMensual);
            response.put("topeIngresosAnuales", TOPE_INGRESOS_ANUALES);
            response.put("topeMensualEquivalente", TOPE_INGRESOS_ANUALES / 12);
            response.put("status", 200);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            response.put("error", "Error al calcular la declaración de renta");
            response.put("status", 500);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private double calcularIngresosAnuales(List<Income> ingresos) {
        return ingresos.stream()
                .mapToDouble(Income::getAmount)
                .sum();
    }

    // Método adicional para verificar si es asalariado (80%+ ingresos laborales)
    private boolean esAsalariado(List<Income> ingresos) {
        // Implementar lógica para determinar si al menos el 80% de los ingresos son laborales
        // Esto depende de cómo tengas categorizados los ingresos en tu aplicación
        return true; // Implementación simplificada
    }
}