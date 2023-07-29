package com.spring.boot.application.services.company;

import com.spring.boot.application.common.utils.AppUtil;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.company.CompanyRequest;
import com.spring.boot.application.controller.model.response.company.CompanyResponse;
import com.spring.boot.application.controller.model.response.job.JobResponse;
import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.Job;
import com.spring.boot.application.repositories.*;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private EmailService emailService;
    final private CompanyRepository companyRepository;
    final private SkillJobRepository skillJobRepository;
    final private LanguageJobRepository languageJobRepository;
    final private UserRepository userRepository;
    final private JobRepository jobRepository;

    public CompanyServiceImpl(
            CompanyRepository companyRepository,
            SkillJobRepository skillJobRepository,
            LanguageJobRepository languageJobRepository,
            UserRepository userRepository,
            JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.skillJobRepository = skillJobRepository;
        this.languageJobRepository = languageJobRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Value("${project.sources}")
    private String root;

    @Override
    public Company addCompany(CompanyRequest addCompany) {
        Validator.notNullAndNotEmptyParam(addCompany.getCompanyName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(addCompany.getEmail(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(addCompany.getPhoneNumber(), RestAPIStatus.BAD_PARAMS, "");
        Validator.validEmailAddressRegex(addCompany.getEmail(), RestAPIStatus.BAD_PARAMS, "");

        com.spring.boot.application.entity.Company existCompany = companyRepository.getByCompanyName(addCompany.getCompanyName());

        Validator.mustNull(existCompany, RestAPIStatus.EXISTED, "");

        com.spring.boot.application.entity.Company company = new com.spring.boot.application.entity.Company();
        company.setId(UniqueID.getUUID());
        company.setCompanyName(addCompany.getCompanyName());
        company.setOverview(addCompany.getOverview());
        company.setEmail(addCompany.getEmail());
        company.setPhoneNumber(addCompany.getPhoneNumber());
        company.setSize(addCompany.getSize());
        company.setWebsite(addCompany.getWebsite());

        companyRepository.save(company);
        return company;
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
    public Company updateCompany(String id, CompanyRequest updateCompany) {
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
    public List<CompanyResponse> getListCompany() throws IOException {
        List<CompanyResponse> companies = new ArrayList<>();
        List<Company> c = companyRepository.findAll();
        for (int i = 0; i < c.size(); i++) {
            companies.add(new CompanyResponse(c.get(i),
                    AppUtil.getUrlCompany(c.get(i), true),
                    AppUtil.getUrlCompany(c.get(i), false)));
        }
        return companies;
    }

    @Override
    public CompanyResponse getCompany(String id) throws IOException {
        Company c = companyRepository.getById(id);
        Validator.notNull(c, RestAPIStatus.NOT_FOUND, "");
        return new CompanyResponse(c, AppUtil.getUrlCompany(c, true),
                AppUtil.getUrlCompany(c, false));
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
