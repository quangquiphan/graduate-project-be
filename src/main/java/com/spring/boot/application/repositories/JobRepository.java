package com.spring.boot.application.repositories;

import com.spring.boot.application.controller.model.response.job.JobResponse;
import com.spring.boot.application.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    Job getById(String id);

    @Query(value = " SELECT j FROM Job j " +
                   " WHERE j.companyId =:companyId AND DATEDIFF(j.expiryDate, CURRENT_DATE) > 0" +
                   " ORDER BY j.expiryDate DESC "
    )
    List<Job> getAllByCompanyId(@Param("companyId") String companyId);

    @Query(value = " SELECT j FROM Job j " +
            " WHERE j.companyId =:companyId AND DATEDIFF(j.expiryDate, CURRENT_DATE) > 0 AND j.categoryJob =:major" +
            " ORDER BY j.expiryDate DESC "
    )
    List<Job> getAllByCompanyId(@Param("companyId") String companyId, @Param("major") String major);

    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.job.JobResponse(j, c) " +
                   " FROM Job j INNER JOIN Company c ON j.companyId = c.id" +
                   " WHERE CONCAT(j.jobName, j.jobPosition, j.city) LIKE %?1% AND DATEDIFF(j.expiryDate, CURRENT_DATE) > 0" +
                   " ORDER BY j.expiryDate DESC "
    )
    List<JobResponse> getBySearchKey(@Param("search") String search);

    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.job.JobResponse(j, c) " +
            " FROM Job j INNER JOIN Company c ON j.companyId = c.id" +
            " WHERE DATEDIFF(j.expiryDate, CURRENT_DATE) > 0" +
            " ORDER BY j.expiryDate DESC "
    )
    List<JobResponse> getAllJobs();

    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.job.JobResponse(j, c) " +
            " FROM Job j INNER JOIN Company c ON j.companyId = c.id" +
            " WHERE DATEDIFF(j.expiryDate, CURRENT_DATE) > 0" +
            " ORDER BY j.expiryDate DESC "
    )
    Page<JobResponse> getAllJobs(Pageable pageable);

    @Query(value = " SELECT COUNT(j.id) FROM Job j WHERE DATEDIFF(j.expiryDate, CURRENT_DATE) > 0"
    )
    int countJobs();
}
