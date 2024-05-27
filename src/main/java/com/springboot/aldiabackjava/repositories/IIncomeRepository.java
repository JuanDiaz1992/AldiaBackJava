package com.springboot.aldiabackjava.repositories;

import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IIncomeRepository extends JpaRepository<Income,Integer> {
    @Query("SELECT i FROM Income i WHERE i.user.idUser = :id_user AND YEAR(i.date) = :year")
    List<Income> findByUserIdAndYear(@Param("id_user") int idUser, @Param("year") String year);


    @Query("SELECT i FROM Income i WHERE i.user.idUser = :id_user AND DATE_FORMAT(i.date, '%Y-%m') = :yearMonth")
    Page<Income> findByUserIdAndYearMonth(@Param("id_user") int idUser, @Param("yearMonth") String yearMonth, Pageable pageable);
}
