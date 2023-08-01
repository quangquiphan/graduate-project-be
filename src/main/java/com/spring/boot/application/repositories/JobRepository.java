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
    List<Job> getAllByCity(String city);

    @Query(value = " SELECT j FROM Job j " +
                   " WHERE j.companyId=:companyId AND DATE_FORMAT(j.expiryDate, 'YYYY-MM-DD') - CURRENT_DATE >= 0" +
                   " ORDER BY j.createdDate DESC "
    )
    List<Job> getAllJobCompany(@Param("companyId") String companyId);

    @Query(value = " SELECT j FROM Job j " +
            " WHERE j.companyId=:companyId AND DATE_FORMAT(j.expiryDate, 'YYYY-MM-DD') - CURRENT_DATE >= 0" +
            " ORDER BY j.createdDate DESC "
    )
    Page<Job> getAllJobCompany(@Param("companyId") String companyId, Pageable pageable);

    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.job.JobResponse(j, c) " +
                   " FROM Job j INNER JOIN Company c ON j.companyId = c.id" +
                   " WHERE DATE_FORMAT(j.expiryDate, 'YYYY-MM-DD') - CURRENT_DATE >= 0" +
                   " AND CONCAT(j.jobName, j.jobPosition) LIKE %?1%" +
                   " ORDER BY j.createdDate DESC "
    )
    List<JobResponse> getBySearchKey(@Param("search") String search);

    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.job.JobResponse(j, c) " +
            " FROM Job j INNER JOIN Company c ON j.companyId = c.id" +
            " WHERE DATE_FORMAT(j.expiryDate, 'YYYY-MM-DD') - CURRENT_DATE >= 0" +
            " ORDER BY j.createdDate DESC "
    )
    List<JobResponse> getAllJobs();
}
