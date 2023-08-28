package com.spring.boot.application.services.experience;

import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.experience.ProjectRequest;
import com.spring.boot.application.controller.model.request.experience.UpdateWorkHistoryRequest;
import com.spring.boot.application.controller.model.request.experience.WorkHistoryRequest;
import com.spring.boot.application.controller.model.response.experience.WorkHistoryResponse;
import com.spring.boot.application.entity.Project;
import com.spring.boot.application.entity.WorkHistory;
import com.spring.boot.application.repositories.ProjectRepository;
import com.spring.boot.application.repositories.WorkHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkHistoryServiceImpl implements WorkHistoryService{
    final private WorkHistoryRepository workHistoryRepository;
    final private ProjectRepository projectRepository;

    public WorkHistoryServiceImpl(
            WorkHistoryRepository workHistoryRepository,
            ProjectRepository projectRepository
    ) {
        this.workHistoryRepository = workHistoryRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public WorkHistoryResponse addWorkHistory(WorkHistoryRequest workHistory) {
        Validator.notNullAndNotEmptyParam(workHistory.getCompanyName(), RestAPIStatus.BAD_PARAMS, "");
        WorkHistory history = newWorkHistory(workHistory);

        List<Project> projects = new ArrayList<>();

        for (int i = 0; i < workHistory.getProjects().size(); i++) {
            Project project = newProject(history.getId(), workHistory.getProjects().get(i));
            projects.add(project);
        }

        workHistoryRepository.save(history);
        projectRepository.saveAll(projects);
        return new WorkHistoryResponse(history, projects);
    }

    @Override
    public WorkHistoryResponse getWorkHistory(String id) {
        WorkHistory history = workHistoryRepository.getById(id);
        Validator.notNull(history, RestAPIStatus.NOT_FOUND, "");

        List<Project> projects = projectRepository.getAllByWorkHistoryId(id);

        return new WorkHistoryResponse(history, projects);
    }

    @Override
    public WorkHistory updateWorkHistory(String id, UpdateWorkHistoryRequest historyRequest) {
        WorkHistory history = workHistoryRepository.getById(id);
        Validator.notNull(history, RestAPIStatus.NOT_FOUND, "");

        return workHistoryRepository.save(editWorkHistory(history, historyRequest));
    }

    @Override
    public String deleteWorkHistory(String id) {
        WorkHistory workHistory = workHistoryRepository.getById(id);
        Validator.notNullAndNotEmpty(workHistory, RestAPIStatus.BAD_REQUEST, "");

        List<Project> projects = projectRepository.getAllByWorkHistoryId(id);

        if (Validator.isValidObject(projects)) {
            projectRepository.deleteAll(projects);
        }

        workHistoryRepository.delete(workHistory);
        return "Delete successfully!";
    }

    private WorkHistory newWorkHistory(WorkHistoryRequest request) {
        WorkHistory history = new WorkHistory();
        history.setId(UniqueID.getUUID());
        history.setCompanyName(request.getCompanyName());
        history.setPosition(request.getPosition());
        history.setFromDate(request.getFromDate());
        history.setToDate(request.getToDate());
        history.setCurrent(request.isCurrent());
        history.setDescription(request.getDescription());
        history.setUserId(request.getUserId());

        return history;
    }

    private WorkHistory editWorkHistory(WorkHistory history, UpdateWorkHistoryRequest request) {
        history.setCompanyName(request.getCompanyName());
        history.setPosition(request.getPosition());
        history.setFromDate(request.getFromDate());
        history.setToDate(request.getToDate());
        history.setCurrent(request.isCurrent());
        history.setDescription(request.getDescription());
        history.setUserId(request.getUserId());

        return history;
    }

    private Project newProject(String workHistoryId, ProjectRequest request) {
        Project project = new Project();
        project.setId(UniqueID.getUUID());
        project.setProjectName(request.getProjectName());
        project.setFromDate(request.getFromDate());
        project.setToDate(request.getToDate());
        project.setTeamSize(request.getTeamSize());
        project.setTechnology(request.getTechnology());
        project.setDescription(request.getDescription());
        project.setWorkHistoryId(workHistoryId);

        return project;
    }
}
