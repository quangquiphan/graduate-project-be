package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Session getByAccessToken(String accessToken);
}
