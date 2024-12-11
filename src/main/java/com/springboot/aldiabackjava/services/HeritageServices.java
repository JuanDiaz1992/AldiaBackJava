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

    public ResponseEntity<Map<String, String>> deleteHeritage(int id) {
        Map <String,String>response = new HashMap<>();
        User user = jwtInterceptor.getCurrentUser();
        try{
            Heritages heritage = ihEritageRepository.findById(id).orElse(null);
            if (heritage!=null && heritage.getUser().equals(user)){
                ihEritageRepository.delete(heritage);
                response.put("message","Registro eliminado correctamente");
                response.put("status","200");
                return ResponseEntity.ok().body(response);
            }else{
                response.put("message","Error al eliminar el registro");
                response.put("status","409");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e){
            response.put("message","Error al eliminar el registro");
            response.put("status","409");
            return ResponseEntity.badRequest().body(response);
        }


    }

    public ResponseEntity<Map<String,String>>editHeritage(BasicHeritage heritage){
        Map <String,String>response = new HashMap<>();
        User user = jwtInterceptor.getCurrentUser();
        try{
            Heritages heritageToEdit = ihEritageRepository.findById(heritage.getId()).orElse(null);
            if(heritageToEdit.getUser().equals(user) && heritageToEdit!=null){
                heritageToEdit.setTypeHeritages(heritage.getTypeHeritages());
                heritageToEdit.setDescription(heritage.getDescription());
                heritageToEdit.setAcquisitionDate(heritage.getAcquisitionDate());
                heritageToEdit.setCurrenValue(heritage.getCurrenValue());
                heritageToEdit.setAcquisitionValue(heritage.getAcquisitionValue());
                ihEritageRepository.save(heritageToEdit);
                response.put("message", "Registro editado correctamente");
                response.put("status", "200");
                return ResponseEntity.ok().body(response);
            }else{
                response.put("message", "Ah ocurrido un error al editar el registro");
                response.put("status", "409");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e){
            response.put("message", "Ah ocurrido un error al editar el registro");
            response.put("status", "409");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<Map<String, String>> getTotalheritagesService() {
        Map<String, String> response = new HashMap<>();
        User user = jwtInterceptor.getCurrentUser();
        Integer total = 0;

        try {
            List<Heritages> heritages = ihEritageRepository.findByUserIdUser(user.getIdUser());
            if (heritages == null || heritages.isEmpty()) {
                response.put("total", total.toString());
                response.put("message", "No hay registro de patrimonios");
                response.put("status", "409");
                return ResponseEntity.status(409).body(response); // Cambiar al código correcto.
            }

            for (Heritages heritage : heritages) {
                total += heritage.getCurrenValue();
            }

            response.put("total", total.toString());
            response.put("status", "200");
            return ResponseEntity.ok().body(response);

        } catch (Exception e) { // Captura todas las excepciones inesperadas.
            response.put("total", total.toString());
            response.put("message", "Ocurrió un error inesperado");
            response.put("status", "500");
            return ResponseEntity.status(500).body(response);
        }
    }

}
