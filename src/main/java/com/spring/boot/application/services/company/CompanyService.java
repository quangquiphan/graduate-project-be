package com.spring.boot.application.services.company;

import com.spring.boot.application.controller.model.request.company.AddCompany;
import com.spring.boot.application.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CompanyService {
    Company addCompany(AddCompany addCompany);
    Company uploadAvatar(String id, MultipartFile file) throws IOException;
    Company uploadBackground(String id, MultipartFile file) throws IOException;
    Company updateCompany(String id, AddCompany updateCompany);
    Page<Company> getAllCompany(int pageNumber, int pageSize);
    Company getCompany(String id);
    String deleteCompany(String id);
}
