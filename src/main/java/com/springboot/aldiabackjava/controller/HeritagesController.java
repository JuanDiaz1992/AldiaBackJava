package com.springboot.aldiabackjava.controller;

import com.springboot.aldiabackjava.models.heritages.Heritages;
import com.springboot.aldiabackjava.services.HeritagesServices.HeritageServices;
import com.springboot.aldiabackjava.services.HeritagesServices.TypeHeritagesServices;
import com.springboot.aldiabackjava.services.financialServices.requestAndResponses.BasicHeritage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/heritages")
public class HeritagesController {
    public final HeritageServices heritageServices;
    public final TypeHeritagesServices typeHeritagesServices;


    @PostMapping("/saveheritage")
    public ResponseEntity<Map<String,String>> saveHeritage(@RequestBody BasicHeritage basicHeritage){
        return heritageServices.createHeritage(basicHeritage);
    }

    @GetMapping("/getheritages")
    public ResponseEntity<Page<Heritages>> getHeritages(Pageable pageable){
        return heritageServices.getHeritages(pageable);
    }

    @DeleteMapping("/deleteheritage/id/{id}")
    public ResponseEntity<Map<String,String>> delteheritage(@PathVariable("id") int id){
        return heritageServices.deleteHeritage(id);
    }

    @PutMapping("/editHeritage")
    public ResponseEntity<Map<String,String>>editHeritage(@RequestBody BasicHeritage basicHeritage){
        return heritageServices.editHeritage(basicHeritage);
    }

    @GetMapping("/gettotalheritages")
    public ResponseEntity<Map<String,String>>getTotalHeritages(){
        return heritageServices.getTotalheritagesService();
    }

    @GetMapping("/types-heritages")
    public ResponseEntity<Map<String, Object>> getTypesHeritages(){
        return typeHeritagesServices.getAllTypes();
    }
}

