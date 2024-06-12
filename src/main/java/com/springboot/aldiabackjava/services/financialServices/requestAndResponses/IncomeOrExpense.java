package com.springboot.aldiabackjava.services.financialServices.requestAndResponses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.CategoryExpenses;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.CategoryIncomes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeOrExpense {
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date date;
    int amount;
    String description;
    CategoryExpenses categoryExpenses;
    CategoryIncomes categoryIncomes;
    String picture;
}
