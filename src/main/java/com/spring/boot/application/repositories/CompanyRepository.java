package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Company getById(String id);
    Company getByCompanyName(String companyName);
    @Query(value = " SELECT c FROM Company c " +
                   " WHERE CONCAT(c.email, c.companyName) LIKE %?1%")
    Page<Company> getPageCompany(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = " SELECT COUNT(c.id) FROM Company c")
    int countCompanies();
}
