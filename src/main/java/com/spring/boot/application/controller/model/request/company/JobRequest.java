package com.spring.boot.application.controller.model.request.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.spring.boot.application.controller.model.request.skill.LanguageJobRequest;
import com.spring.boot.application.controller.model.request.skill.SkillJobRequest;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class JobRequest {
    private String jobName;
    private String jobPosition;
    private List<LanguageJobRequest> languages;
    private List<SkillJobRequest> skills;
    private String description;
    private String required;
    private String benefited;
    private String address;
    private String salary;
    private String city;
    private Date expiryDate;
    private String categoryJob;
    private String companyId;
}
