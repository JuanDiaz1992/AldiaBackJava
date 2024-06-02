package com.springboot.aldiabackjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AlDiaBackJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlDiaBackJavaApplication.class, args);
    }

}
