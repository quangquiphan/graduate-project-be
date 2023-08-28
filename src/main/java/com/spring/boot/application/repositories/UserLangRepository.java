package com.spring.boot.application.repositories;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.controller.model.response.skill.UserLangResponse;
import com.spring.boot.application.entity.UserLang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLangRepository extends JpaRepository<UserLang, String> {
    UserLang getByLangIdAndStatus(String langId, Status status);
    @Query(value = " SELECT new com.spring.boot.application.controller.model.response.skill.UserLangResponse(ul, l)" +
                   " FROM Language l, UserLang ul WHERE l.id = ul.langId AND ul.userId =:userId")
    List<UserLangResponse> getAllByUserId(@Param("userId") String userId);
}
