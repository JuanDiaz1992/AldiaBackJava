package com.springboot.aldiabackjava.controller;

import com.springboot.aldiabackjava.services.UtilsServices;
import com.springboot.aldiabackjava.services.financialServices.FinancialServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/utils")
@AllArgsConstructor
public class UtilsController {
    private final UtilsServices utilsServices;

    @PostMapping("/getinfoimg")
    public ResponseEntity<Map<String,String>> ckeck(@RequestBody Map<String, String> imgBase64) throws IOException {
         return utilsServices.recognizedText(imgBase64);
    }
}
