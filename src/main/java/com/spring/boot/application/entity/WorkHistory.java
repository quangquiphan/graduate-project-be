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
@Table(name = "work_history")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@EntityListeners(AuditingEntityListener.class)
public class WorkHistory extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "is_current")
    private boolean isCurrent;

    @Column(name = "position")
    private String position;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id", length = 64)
    private String userId;
}
