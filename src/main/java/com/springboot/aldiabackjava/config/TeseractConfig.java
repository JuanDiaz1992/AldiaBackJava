package com.springboot.aldiabackjava.config;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeseractConfig {
    @Bean
    Tesseract teseract(){
        Tesseract tesseract = new Tesseract();
//        tesseract.setDatapath("F:\\Archivos\\Desktop\\DEV\\JAVA\\AlDiaBack\\AlDiaBackJava\\src\\tessdata");
        tesseract.setDatapath("/home/ranaclay/Documentos/AlDiaBack/AlDiaBackJava/src/tessdata");
        tesseract.setLanguage("spa");
        return tesseract;
    }

}
