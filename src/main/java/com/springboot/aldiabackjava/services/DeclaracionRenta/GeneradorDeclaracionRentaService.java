package com.springboot.aldiabackjava.services.DeclaracionRenta;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import com.springboot.aldiabackjava.models.heritages.Heritages;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories.IIncomeRepository;
import com.springboot.aldiabackjava.repositories.heritagesRepositories.IHEritageRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class GeneradorDeclaracionRentaService {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private IIncomeRepository incomeRepository;

    @Autowired
    private IHEritageRepository heritageRepository;

    public ResponseEntity<Map<String, Object>> generarDeclaracionRentaPDF() {
        User user = jwtInterceptor.getCurrentUser();
        if (user == null || user.getProfile() == null) {
            log.error("Usuario o perfil no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // Generar el PDF
            generarContenidoPDF(out, user);
            byte[] pdfBytes = out.toByteArray();

            if (pdfBytes.length == 0) {
                throw new IOException("El PDF generado está vacío");
            }

            log.info("PDF generado exitosamente. Tamaño: {} bytes", pdfBytes.length);

            // Codificar el PDF en Base64 para enviarlo en el JSON
            String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

            // Crear estructura de respuesta con el campo data
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            data.put("pdf", pdfBase64);
            data.put("filename", "declaracion_renta_" + user.getProfile().getDocument() + ".pdf");
            response.put("data", data);
            response.put("success", true);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("Error al generar PDF", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al generar el PDF");
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error("Error al cerrar el stream", e);
            }
        }
    }
    private void generarContenidoPDF(ByteArrayOutputStream out, User user) throws IOException {
        List<Income> ingresos = validarIngresos(incomeRepository.findByUserIdAndYear(user.getIdUser(), 2024));
        List<Heritages> patrimonios = validarPatrimonios(heritageRepository.findByUserIdUser(user.getIdUser()));

        // 1. Configuración inicial del documento
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            // 2. Configurar fuentes
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontItalic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);

            // 3. Contenido del documento
            agregarTitulo(document, fontBold);
            agregarInformacionUsuario(document, user, fontBold, fontNormal);
            agregarIngresos(document, ingresos, fontBold, fontNormal);
            agregarPatrimonio(document, patrimonios, fontBold, fontNormal);
            agregarPiePagina(document, fontItalic);

        } finally {
            // 4. Cerrar el documento (esto es crítico)
            document.close();
        }
    }

    private void agregarTitulo(Document document, PdfFont fontBold) {
        document.add(new Paragraph("DECLARACIÓN DE RENTA 2025 - AÑO GRAVABLE 2024")
                .setFont(fontBold)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));
    }

    private void agregarInformacionUsuario(Document document, User user, PdfFont fontBold, PdfFont fontNormal) {
        document.add(new Paragraph("INFORMACIÓN DEL CONTRIBUYENTE")
                .setFont(fontBold)
                .setFontSize(12));

        document.add(new Paragraph("Nombre: " + safeText(user.getProfile().getName() + " " + user.getProfile().getLastName() + " " + user.getProfile().getSurnamen()))
                .setFont(fontNormal));
        document.add(new Paragraph("Documento: " + safeText(user.getProfile().getDocument()))
                .setFont(fontNormal));
        document.add(new Paragraph("Dirección: " + safeText(user.getProfile().getAddress()))
                .setFont(fontNormal)
                .setMarginBottom(15));
    }

    private void agregarIngresos(Document document, List<Income> ingresos, PdfFont fontBold, PdfFont fontNormal) {
        double totalIngresos = ingresos.stream().mapToDouble(Income::getAmount).sum();
        document.add(new Paragraph("INGRESOS ANUALES 2024")
                .setFont(fontBold)
                .setFontSize(12));
        document.add(new Paragraph("Total ingresos: " + formatCurrency(totalIngresos))
                .setFont(fontNormal)
                .setMarginBottom(15));
    }

    private void agregarPatrimonio(Document document, List<Heritages> patrimonios, PdfFont fontBold, PdfFont fontNormal) {
        double totalPatrimonio = patrimonios.stream().mapToDouble(Heritages::getCurrenValue).sum();
        document.add(new Paragraph("PATRIMONIO")
                .setFont(fontBold)
                .setFontSize(12));
        document.add(new Paragraph("Total patrimonio: " + formatCurrency(totalPatrimonio))
                .setFont(fontNormal)
                .setMarginBottom(10));

        document.add(new Paragraph("Detalle de patrimonios:")
                .setFont(fontBold)
                .setFontSize(12));

        if (!patrimonios.isEmpty()) {
            Table table = new Table(2);
            table.addHeaderCell(new Paragraph("Descripción").setFont(fontBold));
            table.addHeaderCell(new Paragraph("Valor").setFont(fontBold));

            for (Heritages patrimonio : patrimonios) {
                table.addCell(new Paragraph(safeText(patrimonio.getDescription())).setFont(fontNormal));
                table.addCell(new Paragraph(formatCurrency(patrimonio.getCurrenValue())).setFont(fontNormal));
            }
            document.add(table);
        } else {
            document.add(new Paragraph("No se encontraron patrimonios registrados")
                    .setFont(fontNormal)
                    .setFontColor(ColorConstants.GRAY));
        }
    }

    private void agregarPiePagina(Document document, PdfFont fontItalic) {
        document.add(new Paragraph("Documento generado automáticamente por AlDia - " + LocalDate.now())
                .setFont(fontItalic)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20));
    }

    private String formatCurrency(double value) {
        try {
            return NumberFormat.getCurrencyInstance(new Locale("es", "CO")).format(value);
        } catch (Exception e) {
            log.warn("Error formateando moneda, usando formato alternativo", e);
            return String.format("$ %,.2f", value);
        }
    }

    private String safeText(String texto) {
        return texto != null ? texto : "N/A";
    }

    private List<Income> validarIngresos(List<Income> ingresos) {
        return ingresos != null ? ingresos : Collections.emptyList();
    }

    private List<Heritages> validarPatrimonios(List<Heritages> patrimonios) {
        return patrimonios != null ? patrimonios : Collections.emptyList();
    }
}