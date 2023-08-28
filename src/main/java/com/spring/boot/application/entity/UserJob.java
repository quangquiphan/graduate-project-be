package com.spring.boot.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.boot.application.common.BaseEntity;
import com.spring.boot.application.common.enums.JobStatus;
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
@Table(name = "user_job")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@EntityListeners(AuditingEntityListener.class)
public class UserJob extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @Column(name = "job_id", nullable = false, length = 64)
    private String jobId;

    @Column(name = "job_status")
    private JobStatus status;
}
