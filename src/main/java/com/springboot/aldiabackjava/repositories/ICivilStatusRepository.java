package com.springboot.aldiabackjava.repositories;

import com.springboot.aldiabackjava.models.userModels.CivilStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICivilStatusRepository extends CrudRepository<CivilStatus, Integer> {
}
