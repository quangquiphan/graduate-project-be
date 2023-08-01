package com.spring.boot.application.repositories;

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
    Page<Notification> getAllByCompanyId(String companyId, Pageable pageable);
    List<Notification> getAllByCompanyIdAndRead(String companyId, boolean isRead);
}
