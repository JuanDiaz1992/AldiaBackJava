package com.springboot.aldiabackjava.services.financialServices;

import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.CategoryExpenses;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Expense;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories.IExpenseRespository;
import com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories.IExpensesCategoryRespository;
import com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories.IIncomeRepository;
import com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories.IIncomesCategoryRespository;
import com.springboot.aldiabackjava.services.financialServices.requestAndResponses.IncomeOrExpense;
import com.springboot.aldiabackjava.utils.GetDateNow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpensesServices {
    private final IExpenseRespository iExpenseRespository;
    private final IExpensesCategoryRespository iExpensesCategoryRespository;
    private final FinancialServices financialServices;
    @Autowired
    private JwtInterceptor jwtInterceptor;
    //Expenses Services

    public ResponseEntity<Map<String,String>> insertExpensesService(IncomeOrExpense incomeOrExpense) {
        Map<String,String>response = new HashMap<>();
        try {
            User user = jwtInterceptor.getCurrentUser();
            String finalPaht = "";
            if (!incomeOrExpense.getPicture().isEmpty()){
                finalPaht = financialServices.createPicture(user,"expenses",incomeOrExpense, null);
            }
            Expense expense = new Expense().builder()
                    .date(GetDateNow.formatDate(incomeOrExpense.getDate()))
                    .amount(incomeOrExpense.getAmount())
                    .description(incomeOrExpense.getDescription())
                    .picture(finalPaht)
                    .category(incomeOrExpense.getCategoryExpenses())
                    .user(user)
                    .is_planned(incomeOrExpense.getIs_planned())
                    .build();
            iExpenseRespository.save(expense);
            response.put("message","Registro guardado correctamente");
            response.put("status","200");
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            response.put("message","Ah ocurrido un error al guardar el registro");
            response.put("status","409");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    public ResponseEntity<Page<Expense>> getExpensesForMounthService(String date, Pageable pageable){
        User user = jwtInterceptor.getCurrentUser();
        Page<Expense> expenses = iExpenseRespository.findByUserIdAndYearMonthPageable(user.getIdUser(),date, pageable);
        if (expenses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(expenses);
        }
        return ResponseEntity.ok().body(expenses);
    }

    public ResponseEntity<List<Expense>> getExpensesForYearService(String date) {
        User user = jwtInterceptor.getCurrentUser();
        List<Expense> expenses = iExpenseRespository.findByUserIdAndYear(user.getIdUser(),date);
        if (expenses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(expenses);
        }
        return ResponseEntity.ok().body(expenses);
    }

    public ResponseEntity<Map<String,String>> deleteExpenseService(int idExpense){
        User user = jwtInterceptor.getCurrentUser();
        Map<String,String> response = new HashMap<>();
        try {
            Expense expense = iExpenseRespository.findById(idExpense).orElse(null);
            if (expense != null && expense.getUser().equals(user)){
                iExpenseRespository.delete(expense);
                response.put("message","Registro eliminado correctamente");
                response.put("status","200");
                return ResponseEntity.ok().body(response);
            }
            response.put("message","El elemento no existe");
            response.put("status","409");
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e){
            response.put("message","Ah ocurrido un error al eliminar el registro");
            response.put("status","409");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<List>getCategoryExpensesServices(){
        List<CategoryExpenses> list = iExpensesCategoryRespository.findAll();
        return ResponseEntity.ok().body(list);
    }

    public ResponseEntity<Map<String, String>> editExpense(IncomeOrExpense expense) {
        Map<String, String> response = new HashMap<>();
        User user = jwtInterceptor.getCurrentUser();
        String finalPaht = "";
        try {
            Expense expenseToEdit = iExpenseRespository.findById(expense.getId()).orElse(null);
            if (expenseToEdit.getUser().equals(user)) {
                expenseToEdit.setAmount(expense.getAmount());
                expenseToEdit.setDate((GetDateNow.formatDate(expense.getDate())));
                expenseToEdit.setDescription(expense.getDescription());
                expenseToEdit.setCategory(expense.getCategoryExpenses());
                if (!expense.getPicture().isEmpty()) {
                    finalPaht = financialServices.createPicture(user, "expenses", expense, expenseToEdit.getPicture());
                    expenseToEdit.setPicture(finalPaht);
                }
                iExpenseRespository.save(expenseToEdit);
            }
            response.put("message", "Registro editado correctamente");
            response.put("status", "200");
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            response.put("message", "Ah ocurrido un error al editar el registro");
            response.put("status", "409");
            return ResponseEntity.badRequest().body(response);
        }
    }

}
