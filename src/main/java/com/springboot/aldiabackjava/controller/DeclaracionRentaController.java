package com.springboot.aldiabackjava.controller;

import com.springboot.aldiabackjava.services.DeclaracionRenta.DeclaracionRentaService;
import com.springboot.aldiabackjava.services.DeclaracionRenta.GeneradorDeclaracionRentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/declaracion-renta")
public class DeclaracionRentaController {
    private final DeclaracionRentaService declaracionRentaService;
    private final GeneradorDeclaracionRentaService generadorDeclaracionRentaService;

    @GetMapping("/verificar-2025")
    public ResponseEntity<Map<String, Object>> verificarDeclaracion2025() {
        return declaracionRentaService.verificarDeclaracionRenta2025();
    }
    @GetMapping("/generar-declaracion")
    public ResponseEntity<InputStreamResource> generarDeclaracion(){
        return  generadorDeclaracionRentaService.generarDeclaracionRentaPDF();
    }
}
