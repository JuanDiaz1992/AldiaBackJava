package com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories;


import com.springboot.aldiabackjava.models.expensesAndIncomesModels.CategoryIncomes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIncomesCategoryRespository extends JpaRepository<CategoryIncomes,Integer> {
}
