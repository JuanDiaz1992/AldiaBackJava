package com.springboot.aldiabackjava.controller;

import com.springboot.aldiabackjava.models.heritages.Heritages;
import com.springboot.aldiabackjava.services.HeritageServices;
import com.springboot.aldiabackjava.services.financialServices.requestAndResponses.BasicHeritage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/heritages")
public class HeritagesController {
    @Autowired
    HeritageServices heritageServices;

    @PostMapping("/saveheritage")
    public ResponseEntity<Map<String,String>> insertExpenses(@RequestBody BasicHeritage basicHeritage){
        return heritageServices.createHeritage(basicHeritage);
    }

    @GetMapping("/getheritages")
    public ResponseEntity<Page<Heritages>> getHeritages(Pageable pageable){
        return heritageServices.getHeritages(pageable);
    }
}
