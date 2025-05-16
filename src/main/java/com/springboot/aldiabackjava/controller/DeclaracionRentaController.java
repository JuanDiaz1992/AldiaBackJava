package com.springboot.aldiabackjava.controller;

import com.springboot.aldiabackjava.services.DeclaracionRenta.DeclaracionRentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/declaracion-renta")
public class DeclaracionRentaController {
    @Autowired
    private DeclaracionRentaService declaracionRentaService;

    @GetMapping("/verificar-2025")
    public ResponseEntity<Map<String, Object>> verificarDeclaracion2025() {
        return declaracionRentaService.verificarDeclaracionRenta2025();
    }
}
