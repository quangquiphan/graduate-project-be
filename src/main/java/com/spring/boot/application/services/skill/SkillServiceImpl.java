package com.spring.boot.application.services.skill;

import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.skill.SkillRequest;
import com.spring.boot.application.entity.Skill;
import com.spring.boot.application.repositories.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService{
    final private SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill addSkill(SkillRequest skill) {
        Validator.notNullAndNotEmptyParam(skill.getSkillName(), RestAPIStatus.BAD_PARAMS, "Skill name not null");
        Skill s = new Skill();
        s.setId(UniqueID.getUUID());
        s.setSkillName(skill.getSkillName());

        return skillRepository.save(s);
    }

    @Override
    public Skill updateSkill(String id, SkillRequest addSkill) {
        Skill skill = skillRepository.getById(id);

        Validator.notNullAndNotEmpty(skill, RestAPIStatus.NOT_FOUND, "");
        Validator.notNullAndNotEmptyParam(addSkill.getSkillName(), RestAPIStatus.BAD_PARAMS, "Skill name not null");

        skill.setSkillName(addSkill.getSkillName());
        return skillRepository.save(skill);
    }

    @Override
    public List<Skill> getAllSkill() {
        return skillRepository.findAll();
    }

    @Override
    public Skill getSkillById(String id) {
        return skillRepository.getById(id);
    }

    @Override
    public String deleteSkill(String id) {
        Skill skill = skillRepository.getById(id);

        Validator.notNullAndNotEmpty(skill, RestAPIStatus.NOT_FOUND, "");

        skillRepository.delete(skill);
        return "Delete successfully!";
    }
}
