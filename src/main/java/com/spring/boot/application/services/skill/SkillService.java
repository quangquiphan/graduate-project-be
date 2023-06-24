package com.spring.boot.application.services.skill;

import com.spring.boot.application.controller.model.request.skill.AddSkill;
import com.spring.boot.application.entity.Skill;

import java.util.List;

public interface SkillService {
    Skill addSkill(AddSkill skill);

    Skill updateSkill(String id, AddSkill addSkill);

    List<Skill> getAllSkill();

    Skill getSkillById(String id);

    String deleteSkill(String id);
}
