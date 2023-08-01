package com.spring.boot.application.controller.model.response.company;

import com.spring.boot.application.entity.Job;
import com.spring.boot.application.entity.Notification;
import com.spring.boot.application.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String userId;
    private String jobId;
    private String firstName;
    private String lastName;
    private String jobName;
    private String content;
    private boolean isRead;
    private Date createdDate;
    private String companyId;

    public NotificationResponse(User user, Job job, Notification notification) {
        this.id = notification.getId();
        this.userId = user.getId();
        this.jobId = job.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.jobName = job.getJobName();
        this.content = notification.getContent();
        this.isRead = notification.isRead();
        this.createdDate = notification.getCreatedDate();
        this.companyId = notification.getCompanyId();
    }
}
