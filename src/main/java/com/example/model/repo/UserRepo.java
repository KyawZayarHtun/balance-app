package com.example.model.repo;

import com.example.model.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findOneByLoginId(String username);
}
