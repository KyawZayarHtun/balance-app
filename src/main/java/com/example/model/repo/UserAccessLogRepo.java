package com.example.model.repo;

import com.example.model.domain.entity.UserAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserAccessLogRepo extends JpaRepository<UserAccessLog, Integer>,
        JpaSpecificationExecutor<UserAccessLog> {

}
