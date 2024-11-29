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
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialServices {
    private final IIncomeRepository iIncomeRepository;
    private final IIncomesCategoryRespository iIncomesCategoryRespository;
    private final IExpenseRespository iExpenseRespository;
    private final IExpensesCategoryRespository iExpensesCategoryRespository;
    @Value("${user.photos.base.path}")
    private String USER_PHOTOS_BASE_PATH;
    @Value("${user.folders.base.path}")
    private String USER_FOLDERS_BASE_PATH;
    @Autowired
    private JwtInterceptor jwtInterceptor;

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


    public String createPicture(User user,String type, IncomeOrExpense incomeOrExpense, String existPicture){
        String dateFormated = GetDateNow.getOnlyYearAndMonth(incomeOrExpense.getDate());
        byte[] decodedBytes = Base64.getDecoder().decode(incomeOrExpense.getPicture());
        String directory = this.USER_PHOTOS_BASE_PATH + user.getUsername() + "/"+type+"/" + dateFormated +"/";
        Path path = Paths.get(directory);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String filename = GetDateNow.getCode() + ".webp";
        Path imagePath = path.resolve(filename);
        try {
            if(existPicture != null && !existPicture.isEmpty()){
                Path currentProfilePicture = Paths.get(this.USER_FOLDERS_BASE_PATH + existPicture);
                if (Files.exists(currentProfilePicture)) {
                    Files.delete(currentProfilePicture);
                }
            }
            Files.write(imagePath, decodedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "/private/img/users/" + user.getUsername() + "/" + type + "/" + dateFormated + "/" + filename;
    }


    public ResponseEntity<Map<String,Integer>> getBalanceService(String date) {
        String year = date.substring(0,4);
        String mount = date.substring(0,7);
        User user = jwtInterceptor.getCurrentUser();
        Map <String,Integer> response = new HashMap<>();
        try {
            List<Expense> expensesForMonth = iExpenseRespository.findByUserIdAndYearMonth(user.getIdUser(),mount);
            List<Income> incomesForMonth = iIncomeRepository.findByUserIdAndYearMonth(user.getIdUser(), mount);
            List<Expense> expensesForYear = iExpenseRespository.findByUserIdAndYear(user.getIdUser(),year);
            List<Income> incomesForYear = iIncomeRepository.findByUserIdAndYear(user.getIdUser(), year);
            int totalIncomesForMonth = FinancialServices.sumNumbers(incomesForMonth,null);
            int totalIncomesForYear = FinancialServices.sumNumbers(incomesForYear,null);
            int totalExpenseForMonth = FinancialServices.sumNumbers(null,expensesForMonth);
            int totalExpensesForYear = FinancialServices.sumNumbers(null,expensesForYear);
            int totalBalanceForMonth = totalIncomesForMonth - totalExpenseForMonth;
            int totalBalanceForYear = totalIncomesForYear - totalExpensesForYear;
            response.put("balanceYear",totalBalanceForYear);
            response.put("balanceMounth",totalBalanceForMonth);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
    static public int sumNumbers(List<Income> incomes, List<Expense> expenses){
        AtomicInteger totalCash = new AtomicInteger(0);
        if (expenses != null && !expenses.isEmpty()) {
            expenses.forEach(expense -> totalCash.addAndGet(expense.getAmount()));
        }
        if (incomes != null && !incomes.isEmpty()) {
            incomes.forEach(income -> totalCash.addAndGet(income.getAmount()));
        }
        return totalCash.get();
    }
}
