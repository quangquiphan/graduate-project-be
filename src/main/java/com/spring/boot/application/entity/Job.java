package com.spring.boot.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.boot.application.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job")
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public class Job extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_position")
    private String jobPosition;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "required", columnDefinition = "TEXT")
    private String required;

    @Column(name = "benefited", columnDefinition = "TEXT")
    private String benefited;

    @Column(name = "salary")
    private String salary;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "category_job")
    private String categoryJob;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "company_id")
    private String companyId;
}
