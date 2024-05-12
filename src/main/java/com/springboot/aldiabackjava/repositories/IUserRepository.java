package com.springboot.aldiabackjava.repositories;

import com.springboot.aldiabackjava.models.userModels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
