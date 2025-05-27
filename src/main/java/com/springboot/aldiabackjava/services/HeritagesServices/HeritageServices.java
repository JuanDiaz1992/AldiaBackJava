package com.springboot.aldiabackjava.services.HeritagesServices;

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

import java.util.*;


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
                        .acquisitionDate(heritages.getAcquisitionDate())
                        .location(heritages.getLocation())
                        .percentage(heritages.getPercentage())
                        .currenValue(heritages.getCurrenValue())
                        .user(user)
                        .description(heritages.getDescription()).build();
                ihEritageRepository.save(heritagesToCreate);
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
                heritageToEdit.setLocation(heritage.getLocation());
                heritageToEdit.setPercentage(heritage.getPercentage());
                heritageToEdit.setAcquisitionDate(heritage.getAcquisitionDate());
                heritageToEdit.setCurrenValue(heritage.getCurrenValue());
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

    public ResponseEntity<Map<String, Object>> getDetailedHeritagesService() {
        Map<String, Object> response = new HashMap<>();
        User user = jwtInterceptor.getCurrentUser();
        int total = 0;

        try {
            List<Heritages> heritages = ihEritageRepository.findByUserIdUser(user.getIdUser());

            if (heritages == null || heritages.isEmpty()) {
                response.put("total", total);
                response.put("heritages", Collections.emptyList());
                response.put("message", "No hay registros de patrimonios");
                response.put("status", HttpStatus.OK.value());
                return ResponseEntity.ok().body(response);
            }

            // Calcular el total y preparar la lista de detalles
            List<Map<String, Object>> heritageDetails = new ArrayList<>();

            for (Heritages heritage : heritages) {
                total += heritage.getCurrenValue();

                Map<String, Object> detail = new HashMap<>();
                detail.put("id", heritage.getIdHeritage());
                detail.put("description", heritage.getDescription());
                detail.put("value", heritage.getCurrenValue());
                detail.put("type", heritage.getTypeHeritages() != null ?
                        heritage.getTypeHeritages().getName() : "Sin tipo");
                detail.put("acquisitionDate", heritage.getAcquisitionDate());
                detail.put("location", heritage.getLocation());
                detail.put("percentage", heritage.getPercentage());

                heritageDetails.add(detail);
            }

            response.put("total", total);
            response.put("heritages", heritageDetails);
            response.put("count", heritages.size());
            response.put("status", HttpStatus.OK.value());

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            log.error("Error al obtener patrimonios: ", e);

            response.put("total", 0);
            response.put("heritages", Collections.emptyList());
            response.put("message", "Ocurrió un error al obtener los patrimonios");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.internalServerError().body(response);
        }
    }

}
