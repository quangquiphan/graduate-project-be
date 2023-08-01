package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.UserJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJobRepository extends JpaRepository<UserJob, String> {
}
