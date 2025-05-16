package com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories;

import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import com.springboot.aldiabackjava.models.userModels.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IIncomeRepository extends JpaRepository<Income, Integer> {
    @Query("SELECT i FROM Income i WHERE i.user.idUser = :id_user AND YEAR(i.date) = :year")
    List<Income> findByUserIdAndYear(@Param("id_user") int idUser, @Param("year") int year);


    @Query("SELECT i FROM Income i WHERE i.user.idUser = :id_user AND DATE_FORMAT(i.date, '%Y-%m') = :yearMonth")
    Page<Income> findByUserIdAndYearMonthPageable(@Param("id_user") int idUser, @Param("yearMonth") String yearMonth, Pageable pageable);


    @Query("SELECT i FROM Income i WHERE i.user.idUser = :id_user AND DATE_FORMAT(i.date, '%Y-%m') = :yearMonth")
    List<Income> findByUserIdAndYearMonth(@Param("id_user") int idUser, @Param("yearMonth") String yearMonth);

    List<Income> findByUser(User user);
}

