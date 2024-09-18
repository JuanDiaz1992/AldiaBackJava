package com.springboot.aldiabackjava.controller;


import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Expense;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import com.springboot.aldiabackjava.services.financialServices.FinancialServices;
import com.springboot.aldiabackjava.services.financialServices.requestAndResponses.IncomeOrExpense;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/financial")
@AllArgsConstructor
public class FinancialDataController {
    @Autowired
    private final FinancialServices financialServices;

    //Incomes

    @GetMapping("/incomes/month/{month}")
    public ResponseEntity<Page<Income>> getIncomesForMounth(@PathVariable("month") String date, Pageable pageable){
        return financialServices.getIncomesForMounthService(date, pageable);
    }
    @GetMapping("/incomes/year/{year}")
    public ResponseEntity<List<Income>> getIncomesForYear(@PathVariable("year") String date){
        return financialServices.getIncomesForYearService(date);
    }

    @PostMapping("/incomes")
    public ResponseEntity<Map<String,String>> insertIncome(@RequestBody IncomeOrExpense income){
        return financialServices.insertIncomesService(income);
    }

    @DeleteMapping("/incomes/delete/id/{id}")
    public ResponseEntity<Map<String,String>> deleteIncome(@PathVariable("id") int id){
        return financialServices.deleteIncomeService(id);
    }


    //Expenses
    @GetMapping("/expenses/month/{month}")
    public ResponseEntity<Page<Expense>> getExpensesForMounth(@PathVariable("month") String date, Pageable pageable){
        return financialServices.getExpensesForMounthService(date, pageable);
    }

    @GetMapping("/expenses/year/{date}")
    public ResponseEntity<List<Expense>> getExpnesesForYear(@PathVariable("date") String date){
        return financialServices.getExpensesForYearService(date);
    }

    @PostMapping("/expenses")
    public ResponseEntity<Map<String,String>> insertExpenses(@RequestBody IncomeOrExpense expense){
        return financialServices.insertExpensesService(expense);
    }

    @DeleteMapping("/expenses/delete/id/{id}")
    public ResponseEntity<Map<String,String>> deleteExpense(@PathVariable("id") int id){
        return financialServices.deleteExpenseService(id);
    }

    //Expenses and Incomes together
    @GetMapping("/allAmount/month/{month}")
    public ResponseEntity<Map<String,Integer>> getExpensesAndIncomesAmountForMounth(@PathVariable("month") String date){
        return financialServices.getIncomesAndExpensesAmount(date);
    }

    @GetMapping("/allAmount/year/{year}")
    public ResponseEntity<Map<String,Integer>> getExpensesAndIncomesAmountForYear(@PathVariable("year") String date){
        return financialServices.getIncomesAndExpensesYear(date);
    }

    @GetMapping("/categories/{typeCategory}")
    public ResponseEntity<List> getCategorys(@PathVariable("typeCategory") String typeCategory){
        if (typeCategory.equals("incomes")){
            return financialServices.getCategoryIncomesServices();
        }else{
            return financialServices.getCategoryExpensesServices();
        }
    }


}
