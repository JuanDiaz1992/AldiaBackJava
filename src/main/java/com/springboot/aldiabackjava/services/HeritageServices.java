package com.springboot.aldiabackjava.services;

import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.models.heritages.Heritages;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.heritagesRepositories.IHEritageRepository;
import com.springboot.aldiabackjava.services.financialServices.requestAndResponses.BasicHeritage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class HeritageServices {
    @Autowired
    private IHEritageRepository ihEritageRepository;
    @Autowired
    private JwtInterceptor jwtInterceptor;


    public ResponseEntity<Map<String, String>> createHeritage(BasicHeritage heritages){
        User user = jwtInterceptor.getCurrentUser();
        Map <String,String> response = new HashMap<>();
        try {
            if (heritages.getTypeHeritages()!=null && !heritages.getAcquisitionDate().equals("") && heritages.getCurrenValue()>0){
                Heritages heritagesToCreate = new Heritages().builder()
                        .typeHeritages(heritages.getTypeHeritages())
                        .acquisitionValue(heritages.getAcquisitionValue())
                        .acquisitionDate(heritages.getAcquisitionDate())
                        .currenValue(heritages.getCurrenValue())
                        .user(user)
                        .description(heritages.getDescription()).build();
                ihEritageRepository.equals(heritagesToCreate);
                response.put("message","Registro guardado correctamente");
                response.put("status","200");
                return ResponseEntity.ok().body(response);
            }else{
                response.put("message","Los datos no están completos");
                response.put("status","409");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e){
            response.put("message","Ah ocurrido un error al guardar el registro");
            response.put("status","409");
            return ResponseEntity.badRequest().body(response);
        }

    }

    public ResponseEntity<Page<Heritages>> getHeritages(Pageable pageable) {
        User user = jwtInterceptor.getCurrentUser();
       Page<Heritages> heritagePage = ihEritageRepository.findByUserIdPageable(user.getIdUser(), pageable);
        log.info(heritagePage.toString());
        if(heritagePage.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(heritagePage);
        }
        return ResponseEntity.ok().body(heritagePage);
    }

}
