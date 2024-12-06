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
        log.info("here");
        return heritageServices.getTotalheritagesService();
    }
}

