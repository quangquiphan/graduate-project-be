package com.spring.boot.application.common.auth;

import com.spring.boot.application.common.enums.Gender;
import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.enums.YearExperience;
import com.spring.boot.application.common.utils.AppUtil;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.response.company.AuthCompany;
import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

public class AuthUser implements UserDetails {
    @Getter
    private String id;
    @Getter
    private String avatar;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String email;
    @Getter
    private String phoneNumber;
    @Getter
    private Date dateOfBirth;
    @Getter
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Getter
    private String summary;
    @Getter
    private String major;
    @Getter
    private String link;
    @Getter
    private String address;
    @Getter
    private String position;
    @Getter
    private String cv;
    @Getter
    @Enumerated(EnumType.STRING)
    private YearExperience yearExperience;
    @Getter
    private UserRole role;
    @Getter
    @Enumerated(EnumType.STRING)
    private Status status;

    @Getter
    private AuthCompany company;

    public AuthUser(User user) {
        this.id = user.getId();
        this.avatar = getUrl(user);
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOfBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.yearExperience = user.getYearExperience();
        this.role = user.getRole();
        this.summary = user.getSummary();
        this.position = user.getPosition();
        this.major = user.getMajor();
        this.link = user.getLink();
        this.status = user.getStatus();
        this.cv = AppUtil.getUrlUser(user, true);
    }

    public AuthUser(User user, AuthCompany c) {
        this.id = user.getId();
        this.avatar = getUrl(user);
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOfBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.yearExperience = user.getYearExperience();
        this.role = user.getRole();
        this.summary = user.getSummary();
        this.position = user.getPosition();
        this.major = user.getMajor();
        this.link = user.getLink();
        this.status = user.getStatus();
        this.cv = AppUtil.getUrlUser(user, true);
        this.company = c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
    private String getUrl(User user) {
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "User not found");
        if (Validator.isValidParam(user.getAvatar()))
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/static/images/")
                    .path(user.getAvatar())
                    .toUriString();
        return "";
    }
}
