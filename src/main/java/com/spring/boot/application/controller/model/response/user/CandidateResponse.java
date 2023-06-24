package com.spring.boot.application.controller.model.response.user;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.enums.YearExperience;
import com.spring.boot.application.controller.model.response.experience.WorkHistoryResponse;
import com.spring.boot.application.entity.Education;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.entity.WorkHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResponse {
    private String id;
    private String avatar;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private YearExperience yearExperience;
    private String cv;
    private Status status;
    private Date createdDate;
    private Date updatedDate;

    private List<WorkHistoryResponse> workHistories;
    private List<Education> educations;

    public CandidateResponse(User user, String url, String urlCV,
                             List<WorkHistoryResponse> workHistories, List<Education> educations
    ) {
        this.id = user.getId();
        this.avatar = url;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.yearExperience = user.getYearExperience();
        this.cv = urlCV;
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.updatedDate = user.getUpdatedDate();
        this.workHistories = workHistories;
        this.educations = educations;
    }
}
