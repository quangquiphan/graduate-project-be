package com.spring.boot.application.controller.model.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.spring.boot.application.common.enums.Gender;
import com.spring.boot.application.common.enums.YearExperience;
import com.spring.boot.application.common.utils.Constant;
import com.spring.boot.application.controller.model.request.education.EducationRequest;
import com.spring.boot.application.controller.model.request.experience.WorkHistoryRequest;
import com.spring.boot.application.controller.model.request.skill.LanguageRequest;
import com.spring.boot.application.controller.model.request.skill.SkillRequest;
import com.spring.boot.application.controller.model.request.skill.UserLangRequest;
import com.spring.boot.application.controller.model.request.skill.UserSkillRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class SubmitProfile {
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private Gender gender;
    private String summary;
    private String position;
    private String major;
    private String link;
    private String address;
    private YearExperience yearExperience;

    private List<UserSkillRequest> skills;
    private List<UserLangRequest> languages;
}
