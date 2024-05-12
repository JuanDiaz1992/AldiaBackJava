package com.springboot.aldiabackjava.repositories;

import com.springboot.aldiabackjava.models.userModels.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProfileRepository extends CrudRepository<Profile, Integer> {
}
