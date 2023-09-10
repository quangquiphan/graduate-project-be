package com.spring.boot.application.repositories;

import com.spring.boot.application.common.enums.JobStatus;
import com.spring.boot.application.controller.model.response.job.UserJobResponse;
import com.spring.boot.application.controller.model.response.user.UserResponse;
import com.spring.boot.application.entity.UserJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJobRepository extends JpaRepository<UserJob, String> {
    List<UserJob> getAllByJobId(String jobId);
    UserJob getById(String id);
    @Query(value = " SELECT DISTINCT uj FROM UserJob uj " +
            " WHERE uj.userId =:userId")
    List<UserJob> getByUserId(@Param("userId") String userId);
    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.job.UserJobResponse(u, uj) " +
            " FROM User u, UserJob uj where u.id = uj.userId AND uj.jobId =:jobId AND uj.status =:status")
    List<UserJobResponse> getAllByJobIdAndStatus(@Param("jobId") String jobId, @Param("status") JobStatus status);
}
