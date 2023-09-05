package com.spring.boot.application.controller.model.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private int totalCandidates;
    private int totalJobs;
    private int totalCompanies;
}
