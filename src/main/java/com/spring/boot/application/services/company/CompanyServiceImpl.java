package com.spring.boot.application.services.company;

import com.spring.boot.application.common.utils.AppUtil;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.company.AddCompany;
import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.repositories.CompanyRepository;
import com.spring.boot.application.services.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private EmailService emailService;
    final private CompanyRepository companyRepository;

    public CompanyServiceImpl(
            CompanyRepository companyRepository
    ) {
        this.companyRepository = companyRepository;
    }

    @Value("${project.sources}")
    private String root;

    @Override
    public Company addCompany(AddCompany addCompany) {
        Validator.notNullAndNotEmptyParam(addCompany.getCompanyName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(addCompany.getEmail(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(addCompany.getPhoneNumber(), RestAPIStatus.BAD_PARAMS, "");
        Validator.validEmailAddressRegex(addCompany.getEmail(), RestAPIStatus.BAD_PARAMS, "");

        Company existCompany = companyRepository.getByCompanyName(addCompany.getCompanyName());

        Validator.mustNull(existCompany, RestAPIStatus.EXISTED, "");

        Company company = new Company();
        company.setId(UniqueID.getUUID());
        company.setAvatar(null);
        company.setBackground(null);
        company.setCompanyName(addCompany.getCompanyName());
        company.setOverview(addCompany.getOverview());
        company.setEmail(addCompany.getEmail());
        company.setPhoneNumber(addCompany.getPhoneNumber());
        company.setSize(addCompany.getSize());
        company.setWebsite(addCompany.getWebsite());
        return companyRepository.save(company);
    }

    @Override
    public Company uploadAvatar(String id, MultipartFile file) throws IOException {
        Company company = companyRepository.getById(id);
        Validator.notNullAndNotEmpty(company, RestAPIStatus.NOT_FOUND, "");

        return upload(company, "images/", file, true);
    }

    @Override
    public Company uploadBackground(String id, MultipartFile file) throws IOException {
        Company company = companyRepository.getById(id);
        Validator.notNullAndNotEmpty(company, RestAPIStatus.NOT_FOUND, "");

        return upload(company, "images/", file, false);
    }

    @Override
    public Company updateCompany(String id, AddCompany updateCompany) {
        Company company = companyRepository.getById(id);
        Validator.notNullAndNotEmpty(company, RestAPIStatus.NOT_FOUND, "");
        Validator.notNullAndNotEmptyParam(updateCompany.getCompanyName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(updateCompany.getEmail(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(updateCompany.getPhoneNumber(), RestAPIStatus.BAD_PARAMS, "");
        Validator.validEmailAddressRegex(updateCompany.getEmail(), RestAPIStatus.BAD_PARAMS, "");

        company.setCompanyName(updateCompany.getCompanyName());
        company.setOverview(updateCompany.getOverview());
        company.setEmail(updateCompany.getEmail());
        company.setPhoneNumber(updateCompany.getPhoneNumber());
        company.setSize(updateCompany.getSize());
        company.setWebsite(updateCompany.getWebsite());
        return companyRepository.save(company);
    }

    @Override
    public Page<Company> getAllCompany(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return companyRepository.findAll(pageRequest);
    }

    @Override
    public Company getCompany(String id) {
        return companyRepository.getById(id);
    }

    @Override
    public String deleteCompany(String id) {
        Company company = companyRepository.getById(id);
        Validator.notNullAndNotEmpty(company, RestAPIStatus.NOT_FOUND, "");

        companyRepository.delete(company);
        return "Delete successfully!";
    }

    private Company upload(Company company, String path, MultipartFile file, boolean isAvatar) throws IOException {
        // File name
        String name = company.getId() + "." + AppUtil.getFileExtension(file.getOriginalFilename());

        // Full path
        String filePath = root + path + name;

        // create folder if not create
        File folder = new File(root);

        if (!folder.exists()) {
            folder.mkdir();
        }

        // file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));

        if (isAvatar) {
            company.setAvatar(name);
            return companyRepository.save(company);
        }

        company.setBackground(name);
        return companyRepository.save(company);
    }
}
