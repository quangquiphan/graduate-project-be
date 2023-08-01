package com.spring.boot.application.controller.model.response.user;

import com.spring.boot.application.common.enums.Gender;
import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.enums.YearExperience;
import com.spring.boot.application.controller.model.response.experience.WorkHistoryResponse;
import com.spring.boot.application.entity.Education;
import com.spring.boot.application.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String avatar;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date dateOFBirth;
    private Gender gender;
    private String summary;
    private String major;
    private String link;
    private String address;
    private String position;
    private YearExperience yearExperience;
    private String cv;
    private UserRole role;
    private Status status;
    private Date createdDate;
    private Date updatedDate;

    private List<WorkHistoryResponse> workHistories;
    private List<Education> educations;

    public UserResponse(User user) {
        this.id = user.getId();
        this.avatar = user.getAvatar();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOFBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.yearExperience = user.getYearExperience();
        this.cv = user.getCv();
        this.role = user.getRole();
        this.summary = user.getSummary();
        this.position = user.getPosition();
        this.major = user.getMajor();
        this.link = user.getLink();
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.updatedDate = user.getUpdatedDate();
    }

    public UserResponse(User user, String url, String urlCV) {
        this.id = user.getId();
        this.avatar = url;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOFBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.yearExperience = user.getYearExperience();
        this.cv = urlCV;
        this.role = user.getRole();
        this.summary = user.getSummary();
        this.position = user.getPosition();
        this.major = user.getMajor();
        this.link = user.getLink();
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.updatedDate = user.getUpdatedDate();
    }

    public UserResponse(User user, String url, String urlCV,
                        List<WorkHistoryResponse> workHistories, List<Education> educations
    ) {
        this.id = user.getId();
        this.avatar = url;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOFBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.yearExperience = user.getYearExperience();
        this.cv = urlCV;
        this.summary = user.getSummary();
        this.position = user.getPosition();
        this.major = user.getMajor();
        this.link = user.getLink();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.updatedDate = user.getUpdatedDate();
        this.workHistories = workHistories;
        this.educations = educations;
    }
}
