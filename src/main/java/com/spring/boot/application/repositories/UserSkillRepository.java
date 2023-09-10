package com.spring.boot.application.repositories;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.controller.model.response.skill.UserSkillResponse;
import com.spring.boot.application.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, String> {
    UserSkill getBySkillIdAndStatus(String skillId, Status status);

    @Query(value = " SELECT us FROM UserSkill us WHERE us.userId =:userId")
    List<UserSkill> getAllByUser(String userId);

    @Query(value = " SELECT DISTINCT new com.spring.boot.application.controller.model.response.skill.UserSkillResponse(us, s)" +
                   " FROM Skill s, UserSkill us WHERE s.id = us.skillId AND us.userId =:userId")
    List<UserSkillResponse> getAllByUserId(@Param("userId") String userId);
}
