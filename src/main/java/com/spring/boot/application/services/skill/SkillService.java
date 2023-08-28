package com.spring.boot.application.services.skill;

import com.spring.boot.application.controller.model.request.skill.SkillRequest;
import com.spring.boot.application.entity.Skill;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SkillService {
    Skill addSkill(SkillRequest skill);
    Skill updateSkill(String id, SkillRequest addSkill);
    List<Skill> getAllSkill();
    Page<Skill> getPageSkill(int pageNumber, int pageSize);
    Page<Skill> getPageSkill(String keyword, int pageNumber, int pageSize);
    Skill getSkillById(String id);
    String deleteSkill(String id);
}
