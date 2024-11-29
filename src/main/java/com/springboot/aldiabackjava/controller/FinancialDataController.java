package com.springboot.aldiabackjava.controller;


import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Expense;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import com.springboot.aldiabackjava.services.financialServices.ExpensesServices;
import com.springboot.aldiabackjava.services.financialServices.FinancialServices;
import com.springboot.aldiabackjava.services.financialServices.IncomesServices;
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
    private final ExpensesServices expensesServices;
    private final IncomesServices incomesServices;

    //Incomes

    @GetMapping("/incomes/month/{month}")
    public ResponseEntity<Page<Income>> getIncomesForMounth(@PathVariable("month") String date, Pageable pageable){
        return incomesServices.getIncomesForMounthService(date, pageable);
    }
    @GetMapping("/incomes/year/{year}")
    public ResponseEntity<List<Income>> getIncomesForYear(@PathVariable("year") String date){
        return incomesServices.getIncomesForYearService(date);
    }

    @PostMapping("/incomes")
    public ResponseEntity<Map<String,String>> insertIncome(@RequestBody IncomeOrExpense income){
        return incomesServices.insertIncomesService(income);
    }

    @DeleteMapping("/incomes/delete/id/{id}")
    public ResponseEntity<Map<String,String>> deleteIncome(@PathVariable("id") int id){
        return incomesServices.deleteIncomeService(id);
    }


    //Expenses
    @GetMapping("/expenses/month/{month}")
    public ResponseEntity<Page<Expense>> getExpensesForMounth(@PathVariable("month") String date, Pageable pageable){
        return expensesServices.getExpensesForMounthService(date, pageable);
    }

    @GetMapping("/expenses/year/{date}")
    public ResponseEntity<List<Expense>> getExpnesesForYear(@PathVariable("date") String date){
        return expensesServices.getExpensesForYearService(date);
    }

    @PostMapping("/expenses")
    public ResponseEntity<Map<String,String>> insertExpenses(@RequestBody IncomeOrExpense expense){
        return expensesServices.insertExpensesService(expense);
    }

    @DeleteMapping("/expenses/delete/id/{id}")
    public ResponseEntity<Map<String,String>> deleteExpense(@PathVariable("id") int id){
        return expensesServices.deleteExpenseService(id);
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
            return incomesServices.getCategoryIncomesServices();
        }else{
            return expensesServices.getCategoryExpensesServices();
        }
    }

    @PutMapping("/expenses/edit/")
    public ResponseEntity<Map<String,String>> editExpense(@RequestBody IncomeOrExpense expense){
        return expensesServices.editExpense(expense);
    }
    @PutMapping("/incomes/edit/")
    public ResponseEntity<Map<String,String>> editIncome(@RequestBody IncomeOrExpense income){
        return incomesServices.editIncome(income);
    }

    @GetMapping("/getbalance/{date}")
    public ResponseEntity<Map<String,Integer>> getBalance(@PathVariable("date") String date){
        return financialServices.getBalanceService(date);
    }
}
