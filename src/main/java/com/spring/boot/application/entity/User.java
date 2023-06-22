package com.spring.boot.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.boot.application.common.BaseEntity;
import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.enums.YearExperience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public class User extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", length = 64, nullable = false)
    private String id;

    @Column(name = "active_code")
    private String activeCode;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password_salt")
    private String passwordSalt;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "year_experience")
    private YearExperience yearExperience;

    @Column(name = "cv")
    private String cv;

    @Column(name = "user_role")
    private UserRole role;

    @Column(name = "status")
    private Status status;
}
