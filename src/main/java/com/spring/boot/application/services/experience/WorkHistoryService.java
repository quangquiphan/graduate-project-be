package com.spring.boot.application.services.experience;

import com.spring.boot.application.controller.model.request.experience.WorkHistoryRequest;
import com.spring.boot.application.controller.model.response.experience.WorkHistoryResponse;

public interface WorkHistoryService {
    WorkHistoryResponse addWorkHistory(WorkHistoryRequest workHistory);
    WorkHistoryResponse getWorkHistory(String id);
    String deleteWorkHistory(String id);
}
