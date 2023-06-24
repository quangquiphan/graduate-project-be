package com.spring.boot.application.controller.model.response.experience;

import com.spring.boot.application.entity.Project;
import com.spring.boot.application.entity.WorkHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkHistoryResponse {
    private String id;
    private String companyName;
    private Date fromDate;
    private Date toDate;
    private boolean isCurrent;
    private String position;
    private String description;
    private String userId;

    private List<Project> projects;

    public WorkHistoryResponse(WorkHistory workHistory, List<Project> projects) {
        this.id = workHistory.getId();
        this.companyName = workHistory.getCompanyName();
        this.fromDate = workHistory.getFromDate();
        this.toDate = workHistory.getToDate();
        this.isCurrent = workHistory.isCurrent();
        this.position = workHistory.getPosition();
        this.description = workHistory.getDescription();
        this.userId = workHistory.getUserId();
        this.projects = projects;
    }
}
