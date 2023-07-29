package com.spring.boot.application.repositories;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.controller.model.response.user.UserResponse;
import com.spring.boot.application.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User getById(String id);
    User getByEmailAndStatus(String email, Status status);
    User getByActiveCodeAndStatus(String activeCode, Status status);
    @Query(value = " SELECT u"
                 + " FROM User u INNER JOIN Session s ON u.id = s.userId"
                 + " WHERE s.accessToken =: token")
    User getByAccessToken(@Param("token") String token);

    @Query(value = "SELECT u FROM User u WHERE u.role=:role")
    Page<UserResponse> getAllByRole(@Param("role") UserRole role, Pageable pageable);
}
