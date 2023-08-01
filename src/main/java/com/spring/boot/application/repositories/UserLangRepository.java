package com.spring.boot.application.repositories;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.entity.UserLang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLangRepository extends JpaRepository<UserLang, String> {
    UserLang getByLangIdAndStatus(String langId, Status status);
}
