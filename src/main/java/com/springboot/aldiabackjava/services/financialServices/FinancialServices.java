package com.springboot.aldiabackjava.services.financialServices;


import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.CategoryExpenses;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.CategoryIncomes;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Expense;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
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


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialServices {
    private final IIncomeRepository iIncomeRepository;
    private final IExpenseRespository iExpenseRespository;
    private final IIncomesCategoryRespository iIncomesCategoryRespository;
    private final IExpensesCategoryRespository iExpensesCategoryRespository;
    @Value("${path.to.prop.name}")
    private String USER_PHOTOS_BASE_PATH;
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

    public ResponseEntity<Map<String,String>> insertIncomesService(IncomeOrExpense incomeOrExpense){
        Map<String,String>response = new HashMap<>();
        try {
            User user = jwtInterceptor.getCurrentUser();
            String finalPaht = "";
            if (!incomeOrExpense.getPicture().isEmpty()){
                finalPaht = this.createPicture(user,"incomes",incomeOrExpense);
            }
            Income income = new Income().builder()
                    .date(GetDateNow.formatDate(incomeOrExpense.getDate()))
                    .amount(incomeOrExpense.getAmount())
                    .description(incomeOrExpense.getDescription())
                    .category(incomeOrExpense.getCategoryIncomes())
                    .picture(finalPaht)
                    .user(user)
                    .build();
            iIncomeRepository.save(income);
            response.put("message","Registro guardado correctamente");
            response.put("status","200");
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            response.put("message","Ah ocurrido un error al guardar el registro");
            response.put("status","409");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
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

    public ResponseEntity<List>getCategoryIncomesServices(){
        List<CategoryIncomes> list = iIncomesCategoryRespository.findAll();
        return ResponseEntity.ok().body(list);
    }


    //Expenses Services

    public ResponseEntity<Map<String,String>> insertExpensesService(IncomeOrExpense incomeOrExpense) {
        Map<String,String>response = new HashMap<>();
        try {
            User user = jwtInterceptor.getCurrentUser();
            String finalPaht = "";
            if (!incomeOrExpense.getPicture().isEmpty()){
                finalPaht = this.createPicture(user,"expenses",incomeOrExpense);
            }
            Expense expense = new Expense().builder()
                    .date(GetDateNow.formatDate(incomeOrExpense.getDate()))
                    .amount(incomeOrExpense.getAmount())
                    .description(incomeOrExpense.getDescription())
                    .picture(finalPaht)
                    .category(incomeOrExpense.getCategoryExpenses())
                    .user(user)
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

    public ResponseEntity<List>getCategoryExpensesServices(){
        List<CategoryExpenses> list = iExpensesCategoryRespository.findAll();
        return ResponseEntity.ok().body(list);
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


    public String createPicture(User user,String type, IncomeOrExpense incomeOrExpense){
        String dateFormated = GetDateNow.getOnlyYearAndMonth(incomeOrExpense.getDate());
        byte[] decodedBytes = Base64.getDecoder().decode(incomeOrExpense.getPicture());
        String directory = this.USER_PHOTOS_BASE_PATH + "img/users/" + user.getUsername() + "/"+type+"/" + dateFormated +"/";
        Path path = Paths.get(directory);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String filename = GetDateNow.getCode() + ".webp";
        Path imagePath = path.resolve(filename);
        try {
            Files.write(imagePath, decodedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "/img/users/" + user.getUsername() + "/" + type + "/" + dateFormated + "/" + filename;
    }

}
