package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    Project getById(String id);
    List<Project> getAllByWorkHistoryId(String workHistoryId);
}
