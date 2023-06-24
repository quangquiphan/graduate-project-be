package com.spring.boot.application.services.experience;

import com.spring.boot.application.controller.model.request.experience.AddWorkHistory;
import com.spring.boot.application.controller.model.response.experience.WorkHistoryResponse;

public interface WorkHistoryService {
    WorkHistoryResponse addWorkHistory(AddWorkHistory workHistory);

    String deleteWorkHistory(String id);
}
