package com.spring.boot.application.repositories;

import com.spring.boot.application.common.enums.JobStatus;
import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.controller.model.response.job.UserJobResponse;
import com.spring.boot.application.controller.model.response.user.UserResponse;
import com.spring.boot.application.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User getById(String id);
    User getByEmailAndStatus(String email, Status status);
    User getByActiveCodeAndStatus(String activeCode, Status status);
    @Query(value = " SELECT u"
                 + " FROM User u INNER JOIN Session s ON u.id = s.userId"
                 + " WHERE s.accessToken =:token")
    User getByAccessToken(@Param("token") String token);
    @Query(value = " SELECT DISTINCT new com.spring.boot.application.controller.model.response.user.UserResponse(u) " +
                   " FROM User u where u.role =:role")
    Page<UserResponse> getAllByRole(@Param("role") UserRole role, Pageable pageable);
    @Query(value = " SELECT DISTINCT u FROM User u WHERE u.companyId =:companyId")
    List<User> getAllByCompanyId(String companyId);
    @Query(value = " SELECT DISTINCT new com.spring.boot.application.controller.model.response.user.UserResponse(u) " +
            " FROM User u where u.companyId=:companyId")
    Page<UserResponse> getAllByCompanyId(@Param("companyId") String companyId, Pageable pageable);
    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.user.UserResponse(u) " +
            " FROM User u " +
            " where CONCAT(u.email, u.firstName, u.lastName, u.phoneNumber) LIKE %?1% AND u.role = 1")
    Page<UserResponse> searchCandidate(@Param("keyword") String keyword, Pageable pageable);
    @Query(value = " SELECT DISTINCT new com.spring.boot.application.controller.model.response.job.UserJobResponse(u) " +
            " FROM User u where u.major =:major AND u.id NOT IN (SELECT uj.userId FROM UserJob uj WHERE uj.jobId =:jobId)")
    List<UserJobResponse> getAllByMajor(@Param("major") String major, @Param("jobId") String jobId);

    @Query(value = " SELECT COUNT(u.id) FROM User u WHERE u.role = 1")
    int countCandidates();
}
