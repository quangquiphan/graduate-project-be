package com.spring.boot.application.controller.model.request.education;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class AddEducation {
    private String schoolName;
    private String description;
    private String course;
    private Date fromDate;
    private Date toDate;
    private boolean isCurrent;
}
