package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, String> {
    Skill getById(String id);
}
