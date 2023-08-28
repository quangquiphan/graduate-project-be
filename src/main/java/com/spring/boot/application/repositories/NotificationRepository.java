package com.spring.boot.application.repositories;

import com.spring.boot.application.controller.model.response.company.NotificationResponse;
import com.spring.boot.application.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    Notification getById(String id);
    @Query(value = " SELECT n FROM Notification n WHERE n.companyId=:companyId ORDER BY n.createdDate DESC")
    List<Notification> getAllByCompanyId(@Param("companyId") String companyId);
    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.company.NotificationResponse(u, j, n)" +
                   " FROM Notification n, Job j, User u" +
                   " WHERE n.companyId=:companyId AND n.userId = u.id AND n.jobId = j.id" +
                   " ORDER BY n.createdDate DESC")
    Page<NotificationResponse> getAllByCompanyId(String companyId, Pageable pageable);
    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.company.NotificationResponse(u, j, n)" +
                   " FROM Notification n, Job j, User u" +
                   " WHERE n.companyId=:companyId AND n.read =:filter AND n.userId = u.id AND n.jobId = j.id" +
                   " ORDER BY n.createdDate DESC")
    Page<NotificationResponse> getAllByCompanyId(String companyId , boolean filter, Pageable pageable);
    List<Notification> getAllByCompanyIdAndRead(String companyId, boolean isRead);
}
