package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, String> {
    List<Education> getAllByUserId(String userId);
}
