package com.springboot.aldiabackjava.controller;

import com.springboot.aldiabackjava.JWT.JwtAuthenticationFilter;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @GetMapping("info")
    public String getInfo(HttpServletRequest request) throws ServletException, IOException {
        return "Accediste :D";



    }
}
