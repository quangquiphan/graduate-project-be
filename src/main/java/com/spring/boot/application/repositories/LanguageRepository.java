package com.spring.boot.application.repositories;

import com.spring.boot.application.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
    Language getById(String id);
    Language getByLanguage(String language);
    @Query(value = " SELECT l FROM Language l ORDER BY l.language ASC")
    List<Language> getAll();
}
