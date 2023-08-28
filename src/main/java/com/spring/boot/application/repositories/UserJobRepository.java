package com.spring.boot.application.repositories;

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
    @Query(value = " SELECT uj FROM UserJob uj " +
            " WHERE uj.userId =:userId")
    List<UserJob> getByUserId(@Param("userId") String userId);
}
