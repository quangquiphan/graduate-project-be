package com.spring.boot.application.repositories;

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
}
