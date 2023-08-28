package com.spring.boot.application.services.experience;

import com.spring.boot.application.controller.model.request.experience.UpdateWorkHistoryRequest;
import com.spring.boot.application.controller.model.request.experience.WorkHistoryRequest;
import com.spring.boot.application.controller.model.response.experience.WorkHistoryResponse;
import com.spring.boot.application.entity.WorkHistory;

public interface WorkHistoryService {
    WorkHistoryResponse addWorkHistory(WorkHistoryRequest workHistory);
    WorkHistoryResponse getWorkHistory(String id);
    WorkHistory updateWorkHistory(String id, UpdateWorkHistoryRequest historyRequest);
    String deleteWorkHistory(String id);
}
