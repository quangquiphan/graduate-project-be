package com.spring.boot.application.controller.model.response.job;

import com.spring.boot.application.common.enums.*;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.entity.UserJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJobResponse {
    private String id;
    private String userId;
    private String jobId;
    private String avatar;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
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
    private JobStatus jobStatus;
    private Date createdDate;
    private Date updatedDate;

    private List<?> skills;
    private List<?> languages;

    public UserJobResponse(User u) {
        this.id = "";
        this.jobStatus = null;
        this.userId = u.getId();
        this.jobId = "";
        this.avatar = u.getAvatar();
        this.firstName = u.getFirstName();
        this.lastName = u.getLastName();
        this.dateOfBirth = u.getDateOfBirth();
        this.gender = u.getGender();
        this.phoneNumber = u.getPhoneNumber();
        this.email = u.getEmail();
        this.address = u.getAddress();
        this.yearExperience = u.getYearExperience();
        this.cv = u.getCv();
        this.role = u.getRole();
        this.summary = u.getSummary();
        this.position = u.getPosition();
        this.major = u.getMajor();
        this.link = u.getLink();
        this.status = u.getStatus();
        this.createdDate = null;
        this.updatedDate = null;
    }

    public UserJobResponse(User u, UserJob uj) {
        this.id = uj.getId();
        this.jobStatus = uj.getStatus();
        this.userId = u.getId();
        this.jobId = uj.getJobId();
        this.avatar = u.getAvatar();
        this.firstName = u.getFirstName();
        this.lastName = u.getLastName();
        this.dateOfBirth = u.getDateOfBirth();
        this.gender = u.getGender();
        this.phoneNumber = u.getPhoneNumber();
        this.email = u.getEmail();
        this.address = u.getAddress();
        this.yearExperience = u.getYearExperience();
        this.cv = u.getCv();
        this.role = u.getRole();
        this.summary = u.getSummary();
        this.position = u.getPosition();
        this.major = u.getMajor();
        this.link = u.getLink();
        this.status = u.getStatus();
        this.createdDate = uj.getCreatedDate();
        this.updatedDate = uj.getUpdatedDate();
    }

    public UserJobResponse(UserJobResponse u, String avatar, String cv,
                           List<?> skills, List<?> languages) {
        this.id = u.getId();
        this.jobStatus = u.getJobStatus();
        this.userId = u.getUserId();
        this.jobId = u.getJobId();
        this.avatar = avatar;
        this.firstName = u.getFirstName();
        this.lastName = u.getLastName();
        this.dateOfBirth = u.getDateOfBirth();
        this.gender = u.getGender();
        this.phoneNumber = u.getPhoneNumber();
        this.email = u.getEmail();
        this.address = u.getAddress();
        this.yearExperience = u.getYearExperience();
        this.cv = cv;
        this.role = u.getRole();
        this.summary = u.getSummary();
        this.position = u.getPosition();
        this.major = u.getMajor();
        this.link = u.getLink();
        this.status = u.getStatus();
        this.createdDate = u.getCreatedDate();
        this.updatedDate = u.getUpdatedDate();
        this.skills = skills;
        this.languages = languages;
    }
}
