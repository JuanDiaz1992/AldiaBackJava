package com.springboot.aldiabackjava.services.financialServices.requestAndResponses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeOrExpense {
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date date;
    int amount;
    String description;
    int category;
    String picture;
}
