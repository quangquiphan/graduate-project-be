package com.spring.boot.application.repositories;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, String> {
    UserSkill getBySkillIdAndStatus(String skillId, Status status);
}
