package com.springboot.aldiabackjava.services.financialServices;

import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.CategoryIncomes;
import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import com.springboot.aldiabackjava.models.userModels.User;
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
public class IncomesServices {
    private final IIncomeRepository iIncomeRepository;
    private final IIncomesCategoryRespository iIncomesCategoryRespository;
    private final FinancialServices financialServices;
    @Value("${user.photos.base.path}")
    private String USER_PHOTOS_BASE_PATH;
    @Value("${user.folders.base.path}")
    private String USER_FOLDERS_BASE_PATH;
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
        User user = jwtInterceptor.getCurrentUser();
        Map<String,String>response = new HashMap<>();
        try {
            String finalPaht = "";
            if (!incomeOrExpense.getPicture().isEmpty()){
                finalPaht = financialServices.createPicture(user,"incomes",incomeOrExpense,null);
            }
            Income income = new Income().builder()
                    .date(GetDateNow.formatDate(incomeOrExpense.getDate()))
                    .amount(incomeOrExpense.getAmount())
                    .description(incomeOrExpense.getDescription())
                    .category(incomeOrExpense.getCategoryIncomes())
                    .picture(finalPaht)
                    .user(user)
                    .is_planned(incomeOrExpense.getIs_planned())
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

    public ResponseEntity<Map<String,String>> deleteIncomeService(int incomeId){
        User user = jwtInterceptor.getCurrentUser();
        Map<String,String> response = new HashMap<>();
        try {
            Income income = iIncomeRepository.findById(incomeId).orElse(null);
            if (income != null && income.getUser().equals(user)){
                iIncomeRepository.delete(income);
                response.put("message","Registro eliminado correctamente");
                response.put("status","200");
                return ResponseEntity.ok().body(response);
            }else{
                response.put("message","El elemento no existe");
                response.put("status","409");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e){
            response.put("message","Ah ocurrido un error al eliminar el registro");
            response.put("status","409");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<List>getCategoryIncomesServices(){
        List<CategoryIncomes> list = iIncomesCategoryRespository.findAll();
        return ResponseEntity.ok().body(list);
    }

    public ResponseEntity<Map<String, String>> editIncome(IncomeOrExpense income) {
        User user = jwtInterceptor.getCurrentUser();
        Map<String, String> response = new HashMap<>();
        String finalPaht = "";
        try {
            Income incomeToEdit = iIncomeRepository.findById(income.getId()).orElse(null);
            if (incomeToEdit.getUser().equals(user)) {
                incomeToEdit.setAmount(income.getAmount());
                incomeToEdit.setDate((GetDateNow.formatDate(income.getDate())));
                incomeToEdit.setDescription(income.getDescription());
                incomeToEdit.setCategory(income.getCategoryIncomes());
                if (!income.getPicture().isEmpty()) {
                    finalPaht = financialServices.createPicture(user, "incomes", income, incomeToEdit.getPicture());
                    incomeToEdit.setPicture(finalPaht);
                }
                iIncomeRepository.save(incomeToEdit);
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
