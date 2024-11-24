package com.springboot.aldiabackjava.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TeseractConfig {
    @Bean
    Tesseract teseract(){
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata");
        tesseract.setLanguage("spa");
        return tesseract;
    }

}

//        tesseract.setDatapath("F:\\Archivos\\Desktop\\DEV\\JAVA\\AlDiaBack\\AlDiaBackJava\\src\\tessdata");
//        tesseract.setDatapath("/home/ranaclay/Documentos/AldiaBackJava/src/tessdata");
//Para que esto funcione, se debe instalar teseract en el sistema y se debe crear una variable global con los
//siguentes comandos:
//export TESSDATA_PREFIX=/usr/share/tesseract-ocr/5/
//export TESSERACT_PATH=/usr/bin/tesseract
//source ~/.bashrc