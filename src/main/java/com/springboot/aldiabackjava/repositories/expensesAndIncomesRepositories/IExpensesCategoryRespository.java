package com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories;

import com.springboot.aldiabackjava.models.expensesAndIncomesModels.CategoryExpenses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExpensesCategoryRespository extends JpaRepository<CategoryExpenses,Integer> {
}
