package com.spring.boot.application.repositories;

import com.spring.boot.application.controller.model.response.job.LangJobResponse;
import com.spring.boot.application.entity.LanguageJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageJobRepository extends JpaRepository<LanguageJob, String> {
    @Query(value = " SELECT new  com.spring.boot.application.controller.model.response.job.LangJobResponse(lj, l)"
            + " FROM LanguageJob lj, Language l"
            + " WHERE lj.languageId = l.id AND lj.jobId=:jobId")
    List<LangJobResponse> getAllByJobId(@Param("jobId") String jobId);
}
