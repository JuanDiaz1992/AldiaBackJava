package com.springboot.aldiabackjava.services.HeritagesServices;

import com.springboot.aldiabackjava.models.heritages.TypeHeritages;
import com.springboot.aldiabackjava.repositories.heritagesRepositories.ITypeHeritage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TypeHeritagesServices {
    private final ITypeHeritage iTypeHeritage;

    public ResponseEntity<Map<String, Object>> getAllTypes() {
        Map<String, Object> response = new HashMap<>();
        try{
            List<TypeHeritages> typeHeritagesList = iTypeHeritage.findAll();
            response.put("typeHeritages", typeHeritagesList);
            response.put("status", 200);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.put("status", 400);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
