package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.WorkHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkHistoryRepository extends JpaRepository<WorkHistory, String> {
    WorkHistory getById(String id);
    List<WorkHistory> getAllByUserId(String userId);
}
