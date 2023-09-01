package com.spring.boot.application.repositories;

import com.spring.boot.application.controller.model.response.skill.UserLangResponse;
import com.spring.boot.application.entity.Language;
import com.spring.boot.application.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
    Language getById(String id);
    Language getByLanguage(String language);
    @Query(value = " SELECT l FROM Language l ORDER BY l.language ASC")
    List<Language> getAll();
    @Query(value = " SELECT l FROM Language l WHERE l.language LIKE %?1%")
    Page<Language> getPageLang(String name, Pageable pageable);
    @Query(value = " SELECT l FROM Language l ORDER BY l.language ASC")
    Page<Language> getPageLang(Pageable pageable);
}
