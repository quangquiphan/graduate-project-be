package com.spring.boot.application.controller.model.response.user;

import com.spring.boot.application.entity.Education;
import com.spring.boot.application.entity.WorkHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitProfileResponse {
    private List<Education> educations;
    private List<WorkHistory> workHistories;
}
