package com.spring.boot.application.services.company;

import com.spring.boot.application.controller.model.request.company.CompanyRequest;
import com.spring.boot.application.controller.model.response.company.CompanyResponse;
import com.spring.boot.application.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CompanyService {
    Company addCompany(CompanyRequest addCompany);
    Company uploadAvatar(String id, MultipartFile file) throws IOException;
    Company updateCompany(String id, CompanyRequest updateCompany);
    Page<Company> getAllCompany(int pageNumber, int pageSize);
    Page<Company> searchCompany(String keyword, int pageNumber, int pageSize);
    List<CompanyResponse> getListCompany() throws IOException;
    CompanyResponse getCompany(String id) throws IOException;
    String deleteCompany(String id);
}
