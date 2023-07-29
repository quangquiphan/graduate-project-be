package com.spring.boot.application.services.experience;

import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
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

        WorkHistory history = new WorkHistory();
        history.setId(UniqueID.getUUID());
        history.setCompanyName(workHistory.getCompanyName());
        history.setPosition(workHistory.getPosition());
        history.setFromDate(workHistory.getFromDate());
        history.setToDate(workHistory.getToDate());
        history.setCurrent(workHistory.isCurrent());
        history.setDescription(workHistory.getDescription());
        history.setUserId(workHistory.getUserId());

        List<Project> projects = new ArrayList<>();

        for (int i = 0; i < workHistory.getProjects().size(); i++) {
            Project project = new Project();
            project.setId(UniqueID.getUUID());
            project.setProjectName(workHistory.getProjects().get(i).getProjectName());
            project.setFromDate(workHistory.getProjects().get(i).getFromDate());
            project.setToDate(workHistory.getProjects().get(i).getToDate());
            project.setTeamSize(workHistory.getProjects().get(i).getTeamSize());
            project.setTechnology(workHistory.getProjects().get(i).getTechnology());
            project.setDescription(workHistory.getProjects().get(i).getDescription());
            project.setWorkHistoryId(history.getId());

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
}
