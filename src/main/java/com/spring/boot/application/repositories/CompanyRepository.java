package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Company getById(String id);
    Company getByCompanyName(String companyName);
}
