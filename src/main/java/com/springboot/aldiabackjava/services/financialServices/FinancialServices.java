package com.springboot.aldiabackjava.services.financialServices;


import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Expense;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.IExpenseRespository;
import com.springboot.aldiabackjava.repositories.IIncomeRepository;
import com.springboot.aldiabackjava.services.financialServices.requestAndResponses.IncomeOrExpense;
import lombok.AllArgsConstructor;
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
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
@Slf4j
public class FinancialServices {
    private final IIncomeRepository iIncomeRepository;
    private final IExpenseRespository iExpenseRespository;
    @Autowired
    private JwtInterceptor jwtInterceptor;

    //Incomes Services

    public ResponseEntity<Page<Income>> getIncomesForMounthService(String date, Pageable pageable) {
        User user = jwtInterceptor.getCurrentUser();
        Page<Income> incomesPage = iIncomeRepository.findByUserIdAndYearMonthPageable(user.getIdUser(), date, pageable);

        if (incomesPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incomesPage);
        }

        return ResponseEntity.ok().body(incomesPage);
    }

    public ResponseEntity<List<Income>> getIncomesForYearService(String date) {
        User user = jwtInterceptor.getCurrentUser();
        List<Income> incomes = iIncomeRepository.findByUserIdAndYear(user.getIdUser(),date);
        if (incomes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incomes);
        }
        return ResponseEntity.ok().body(incomes);
    }

    public ResponseEntity<String> insertIncomesService(IncomeOrExpense incomeOrExpense){
        try {
            User user = jwtInterceptor.getCurrentUser();
            Income income = new Income().builder()
                    .date(incomeOrExpense.getDate())
                    .amount(incomeOrExpense.getAmount())
                    .description(incomeOrExpense.getDescription())
                    .picture(incomeOrExpense.getPicture())
                    .category(incomeOrExpense.getCategory())
                    .user(user)
                    .build();
            int id = iIncomeRepository.save(income).getIdIncome();
            return ResponseEntity.ok().body("Registro guardado, id: "+id);
        }catch (Exception e){
            return ResponseEntity.ok().body("Ah ocurrido un error al guardar el registro");
        }
    }

    public ResponseEntity<String> deleteIncomeService(int incomeId){
        User user = jwtInterceptor.getCurrentUser();
        Income income = iIncomeRepository.findById(incomeId).orElse(null);
        if (income != null && income.getUser().equals(user)){
            iIncomeRepository.delete(income);
            return ResponseEntity.ok().body("Registro eliminado correctamente");
        }else {
            return ResponseEntity.badRequest().body("Ah ocurrido un error al eliminar el registro");
        }
    }


    //Expenses Services

    public ResponseEntity<String> insertExpensesService(IncomeOrExpense incomeOrExpense) {
        try {
            User user = jwtInterceptor.getCurrentUser();
            Expense income = new Expense().builder()
                    .date(incomeOrExpense.getDate())
                    .amount(incomeOrExpense.getAmount())
                    .description(incomeOrExpense.getDescription())
                    .picture(incomeOrExpense.getPicture())
                    .category(incomeOrExpense.getCategory())
                    .user(user)
                    .build();
            int id = iExpenseRespository.save(income).getId();
            return ResponseEntity.ok().body("Registro guardado, id: "+id);
        }catch (Exception e){
            return ResponseEntity.ok().body("Ah ocurrido un error al guardar el registro");
        }
    }

    public ResponseEntity<Page<Expense>> getExpensesForMounthService(String date,Pageable pageable){
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

    public ResponseEntity<String> deleteExpenseService(int idExpense){
        User user = jwtInterceptor.getCurrentUser();
        Expense expense = iExpenseRespository.findById(idExpense).orElse(null);
        if (expense != null && expense.getUser().equals(user)){
            iExpenseRespository.delete(expense);
            return ResponseEntity.ok().body("Registro eliminado correctamente");
        }else {
            return ResponseEntity.badRequest().body("Ah ocurrido un error al eliminar el registro");
        }
    }


    //Expenses and Incomes together
    public ResponseEntity<Map<String, Integer>> getIncomesAndExpensesAmount(String date){
        Map<String,Integer> list = new HashMap<>();
        User user = jwtInterceptor.getCurrentUser();
        List<Expense> expenses = iExpenseRespository.findByUserIdAndYearMonth(user.getIdUser(),date);
        List<Income> incomes = iIncomeRepository.findByUserIdAndYearMonth(user.getIdUser(), date);
        AtomicInteger totalCashExpenses = new AtomicInteger();
        AtomicInteger totalCahsIncomes = new AtomicInteger();
        if (!expenses.isEmpty()){
            expenses.forEach(expense -> {
                totalCashExpenses.addAndGet(expense.getAmount());
            });
            list.put("expenses",totalCashExpenses.get());
        }
        if (!incomes.isEmpty()){
            incomes.forEach(income -> {
                totalCahsIncomes.addAndGet(income.getAmount());
            });
            list.put("income",totalCahsIncomes.get());
        }
        if(!list.isEmpty()){
            return ResponseEntity.ok().body(list);
        }else {
            return ResponseEntity.badRequest().body(null);
        }


    }

    public ResponseEntity<Map<String, Integer>> getIncomesAndExpensesYear(String date){
        Map<String,Integer> list = new HashMap<>();
        User user = jwtInterceptor.getCurrentUser();
        List<Expense> expenses = iExpenseRespository.findByUserIdAndYear(user.getIdUser(),date);
        List<Income> incomes = iIncomeRepository.findByUserIdAndYear(user.getIdUser(), date);
        AtomicInteger totalCashExpenses = new AtomicInteger();
        AtomicInteger totalCahsIncomes = new AtomicInteger();
        if (!expenses.isEmpty()){
            expenses.forEach(expense -> {
                totalCashExpenses.addAndGet(expense.getAmount());
            });
            list.put("expenses",totalCashExpenses.get());
        }
        if (!incomes.isEmpty()){
            incomes.forEach(income -> {
                totalCahsIncomes.addAndGet(income.getAmount());
            });
            list.put("income",totalCahsIncomes.get());
        }
        if(!list.isEmpty()){
            return ResponseEntity.ok().body(list);
        }else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
