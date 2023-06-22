package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.WorkHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkHistoryRepository extends JpaRepository<WorkHistory, String> {
}
