package com.spring.boot.application.controller.model.request.experience;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class WorkHistoryRequest {
    private String companyName;
    private Date fromDate;
    private Date toDate;
    private boolean isCurrent;
    private String position;
    private String description;
    private String userId;

    private List<ProjectRequest> projects;
}
