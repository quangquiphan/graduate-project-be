package com.spring.boot.application.controller.model.response.job;

import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private String id;
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
    private String expiryDate;
    private Date createdDate;
    private String companyId;

    List<SkillJobResponse> skills;
    List<LangJobResponse> languages;


    public JobResponse(Job j, String url, String companyName, String companyId,
                       List<SkillJobResponse> sk, List<LangJobResponse> l) {
        this.id = j.getId();
        this.companyId = companyId;
        this.companyAvatar = url;
        this.companyName = companyName;
        this.jobName = j.getJobName();
        this.jobPosition = j.getJobPosition();
        this.description = j.getDescription();
        this.required = j.getRequired();
        this.benefited = j.getBenefited();
        this.salary = j.getSalary();
        this.city = j.getCity();
        this.createdDate = j.getCreatedDate();
        this.expiryDate = j.getExpiryDate();
        this.skills = sk;
        this.languages = l;

    }

    public JobResponse(Job j, Company c) {
        this.id = j.getId();
        this.jobName = j.getJobName();
        this.jobPosition = j.getJobPosition();
        this.description = j.getDescription();
        this.required = j.getRequired();
        this.benefited = j.getBenefited();
        this.salary = j.getSalary();
        this.city = j.getCity();
        this.createdDate = j.getCreatedDate();
        this.expiryDate = j.getExpiryDate();
        this.companyName = c.getCompanyName();
        this.companyAvatar = c.getAvatar();
    }

    public JobResponse(JobResponse j, String avatar) {
        this.id = j.getId();
        this.companyId = j.getCompanyId();
        this.jobName = j.getJobName();
        this.jobPosition = j.getJobPosition();
        this.description = j.getDescription();
        this.required = j.getRequired();
        this.benefited = j.getBenefited();
        this.salary = j.getSalary();
        this.city = j.getCity();
        this.createdDate = j.getCreatedDate();
        this.expiryDate = j.getExpiryDate();
        this.companyName = j.getCompanyName();
        this.companyAvatar = avatar;
    }
}
