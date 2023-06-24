package com.spring.boot.application.controller.model.request.experience;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class AddProject {
    private String projectName;
    private Date fromDate;
    private Date toDate;
    private int teamSize;
    private String description;
    private String technology;
}
