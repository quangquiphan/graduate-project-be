package com.spring.boot.application.services.experience;

import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.experience.ProjectRequest;
import com.spring.boot.application.entity.Project;
import com.spring.boot.application.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService{

    final private ProjectRepository projectRepository;

    public ProjectServiceImpl(
            ProjectRepository projectRepository
    ) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project addProject(ProjectRequest request) {
        Validator.notNullAndNotEmptyParam(request.getProjectName(), RestAPIStatus.BAD_PARAMS, "");
        return projectRepository.save(newProject(request));
    }

    @Override
    public Project getProject(String id) {
        Project p = projectRepository.getById(id);
        Validator.notNull(p, RestAPIStatus.NOT_FOUND, "");

        return p;
    }

    @Override
    public Project updateProject(String id, ProjectRequest project) {
        Project p = projectRepository.getById(id);
        Validator.notNull(p, RestAPIStatus.NOT_FOUND, "");

        return projectRepository.save(updateProject(p, project));
    }

    @Override
    public String deleteProject(String id) {
        Project p = projectRepository.getById(id);
        Validator.notNull(p, RestAPIStatus.NOT_FOUND, "");

        projectRepository.delete(p);
        return "Successfully!";
    }

    private Project newProject(ProjectRequest project) {
        Project p = new Project();

        p.setId(UniqueID.getUUID());
        p.setProjectName(project.getProjectName());
        p.setDescription(project.getDescription());
        p.setFromDate(project.getFromDate());
        p.setToDate(project.getToDate());
        p.setTechnology(project.getTechnology());
        p.setTeamSize(project.getTeamSize());
        p.setWorkHistoryId(project.getWorkHistoryId());

        return p;
    }

    private Project updateProject(Project p, ProjectRequest project) {
        p.setProjectName(project.getProjectName());
        p.setDescription(project.getDescription());
        p.setFromDate(project.getFromDate());
        p.setToDate(project.getToDate());
        p.setTechnology(project.getTechnology());
        p.setTeamSize(project.getTeamSize());

        return p;
    }
}
