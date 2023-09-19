package com.spring.boot.application.controller.model.response.job;

import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.Job;
import com.spring.boot.application.entity.UserJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsAppliedResponse {
    private String id;
    private String jobId;
    private String companyName;
    private String companyAvatar;
    private String jobName;
    private String jobPosition;
    private String description;
    private String required;
    private String benefited;
    private String salary;
    private String city;
    private String categoryJob;
    private Date expiryDate;
    private Date createdDate;
    private String companyId;
    private String address;
    private Date applyDate;

    List<?> skills;
    List<?> languages;

    public JobsAppliedResponse(Job j, Company c, UserJob uj) {
        this.id = uj.getId();
        this.jobId = j.getId();
        this.jobName = j.getJobName();
        this.categoryJob = j.getCategoryJob();
        this.jobPosition = j.getJobPosition();
        this.description = j.getDescription();
        this.required = j.getRequired();
        this.benefited = j.getBenefited();
        this.salary = j.getSalary();
        this.city = j.getCity();
        this.address = j.getAddress();
        this.createdDate = j.getCreatedDate();
        this.expiryDate = j.getExpiryDate();
        this.companyName = c.getCompanyName();
        this.companyId = j.getCompanyId();
        this.applyDate = uj.getCreatedDate();
    }

    public JobsAppliedResponse(JobsAppliedResponse jar, String url,
                               List<?> languages, List<?> skills) {
        this.id = jar.getId();
        this.jobId = jar.getJobId();
        this.jobName = jar.getJobName();
        this.categoryJob = jar.getCategoryJob();
        this.jobPosition = jar.getJobPosition();
        this.description = jar.getDescription();
        this.required = jar.getRequired();
        this.benefited = jar.getBenefited();
        this.salary = jar.getSalary();
        this.city = jar.getCity();
        this.address = jar.getAddress();
        this.createdDate = jar.getCreatedDate();
        this.expiryDate = jar.getExpiryDate();
        this.companyName = jar.getCompanyName();
        this.companyAvatar = url;
        this.companyId = jar.getId();
        this.applyDate = jar.getApplyDate();
        this.skills = skills;
        this.languages = languages;
    }
}
