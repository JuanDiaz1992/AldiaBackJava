package com.springboot.aldiabackjava.repositories.expensesAndIncomesRepositories;

import com.springboot.aldiabackjava.models.expensesAndIncomesModels.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IIncomeRepository extends JpaRepository<Income,Integer> {
    @Query("SELECT i FROM Income i WHERE i.user.idUser = :id_user AND EXTRACT(YEAR FROM i.date) = :year")
    List<Income> findByUserIdAndYear(@Param("id_user") int idUser, @Param("year") String year);

    @Query("SELECT i FROM Income i WHERE i.user.idUser = :id_user AND TO_CHAR(i.date, 'YYYY-MM') = :yearMonth")
    Page<Income> findByUserIdAndYearMonthPageable(@Param("id_user") int idUser, @Param("yearMonth") String yearMonth, Pageable pageable);

    @Query("SELECT i FROM Income i WHERE i.user.idUser = :id_user AND TO_CHAR(i.date, 'YYYY-MM') = :yearMonth")
    List<Income> findByUserIdAndYearMonth(@Param("id_user") int idUser, @Param("yearMonth") String yearMonth);
}
