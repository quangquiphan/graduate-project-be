package com.spring.boot.application.repositories;

import com.spring.boot.application.controller.model.response.job.SkillJobResponse;
import com.spring.boot.application.entity.SkillJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillJobRepository extends JpaRepository<SkillJob, String> {
    @Query(value = " SELECT new  com.spring.boot.application.controller.model.response.job.SkillJobResponse(sj, s)"
                 + " FROM SkillJob sj, Skill s"
                 + " WHERE sj.skillId = s.id AND sj.jobId=:jobId")
    List<SkillJobResponse> getAllByJobId(@Param("jobId") String jobId);
}
