package com.spring.boot.application.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.spring.boot.application.common.utils.Constant;
import com.spring.boot.application.common.utils.DateUtil;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@MappedSuperclass
@JsonInclude(Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public abstract class BaseEntity implements Serializable {

    @CreatedDate
    @Column(name = "created_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE)
    protected Date createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE)
    protected Date updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = DateUtil.convertToUTC(new Date());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = DateUtil.convertToUTC(new Date());
    }

}