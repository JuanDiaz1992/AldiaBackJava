package com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories;

import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Expense;
import com.springboot.aldiabackjava.models.userModels.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IExpenseRespository extends JpaRepository<Expense, Integer> {

    @Query("SELECT e FROM Expense e WHERE e.user.idUser = :id_user AND YEAR(e.date) = :year")
    List<Expense> findByUserIdAndYear(@Param("id_user") int idUser, @Param("year") int year);

    @Query("SELECT e FROM Expense e WHERE e.user.idUser = :id_user AND DATE_FORMAT(e.date, '%Y-%m') = :yearMonth")
    Page<Expense> findByUserIdAndYearMonthPageable(@Param("id_user") int idUser, @Param("yearMonth") String yearMonth, Pageable pageable);

    @Query("SELECT e FROM Expense e WHERE e.user.idUser = :id_user AND DATE_FORMAT(e.date, '%Y-%m') = :yearMonth")
    List<Expense> findByUserIdAndYearMonth(@Param("id_user") int idUser, @Param("yearMonth") String yearMonth);

    List<Expense> user(User user);


    List<Expense> findByUser(User user);
}