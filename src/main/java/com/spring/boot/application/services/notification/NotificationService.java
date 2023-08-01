package com.spring.boot.application.services.notification;

import com.spring.boot.application.controller.model.response.company.NotificationResponse;
import com.spring.boot.application.entity.Notification;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    Notification addNotification(String userId, String jobId, String content);
    Notification markAsRead(String id);
    List<Notification> markAllAsRead(String companyId);
    List<NotificationResponse> getAllNotificationByCompanyId(String companyId);
    Page<Notification> getAllNotificationByCompanyId(String companyId, int pageNumber, int pageSize);
}
