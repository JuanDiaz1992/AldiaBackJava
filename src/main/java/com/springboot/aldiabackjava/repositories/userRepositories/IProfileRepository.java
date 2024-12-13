package com.springboot.aldiabackjava.repositories.userRepositories;

import com.springboot.aldiabackjava.models.userModels.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProfileRepository extends CrudRepository<Profile, Integer> {
    Optional<Profile> findByDocument(String document);
}
