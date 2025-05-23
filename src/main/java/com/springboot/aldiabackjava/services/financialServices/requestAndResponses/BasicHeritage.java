package com.springboot.aldiabackjava.services.financialServices.requestAndResponses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.aldiabackjava.models.heritages.TypeHeritages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BasicHeritage {
    @JsonFormat(pattern = "yyyy-MM-dd")
    int id = 0;
    int idUser;
    private int currenValue;
    private int acquisitionValue;
    Date acquisitionDate;
    String description;
    TypeHeritages typeHeritages;
    String location;
    Integer percentage;

}
