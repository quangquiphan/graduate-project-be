package com.spring.boot.application.controller.model.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.spring.boot.application.controller.model.request.education.AddEducation;
import com.spring.boot.application.controller.model.request.experience.AddWorkHistory;
import com.spring.boot.application.controller.model.request.skill.AddLanguage;
import com.spring.boot.application.controller.model.request.skill.AddSkill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class SubmitProfile {
    private List<AddEducation> educations;
    private List<AddSkill> skills;
    private List<AddLanguage> languages;
    private List<AddWorkHistory> workHistories;
}
