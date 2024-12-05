package com.springboot.aldiabackjava.repositories.heritagesRepositories;

import com.springboot.aldiabackjava.models.heritages.Heritages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IHEritageRepository extends JpaRepository<Heritages, Integer> {
    @Query("SELECT h FROM Heritages h WHERE h.user.idUser = :id_user")
    Page<Heritages> findByUserIdPageable(@Param("id_user") int idUser, Pageable pageable);
}
