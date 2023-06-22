package com.spring.boot.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "session")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public class Session extends BaseEntity implements Serializable {
    @Id
    @Column(name = "access_token", nullable = false, length = 255)
    private String accessToken;

    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "expiry_date")
    private Date expiryDate;
}
