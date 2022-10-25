package com.polugrudov.nottasktrackerapi.api.controller.helper;


import com.polugrudov.nottasktrackerapi.api.exception.NotFoundException;
import com.polugrudov.nottasktrackerapi.store.entity.ProjectEntity;
import com.polugrudov.nottasktrackerapi.store.entity.TaskStateEntity;
import com.polugrudov.nottasktrackerapi.store.repository.ProjectRepository;
import com.polugrudov.nottasktrackerapi.store.repository.TaskStateRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {

    ProjectRepository projectRepository;

    TaskStateRepository taskStateRepository;

    public ProjectEntity getProjectOrThrowException(Long project_id) {
        return projectRepository.findById(project_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" doesn't exist",
                                        project_id)
                        )
                );
    }

    public TaskStateEntity getTaskStatesOrThrowException(Long taskStateId) {
        return taskStateRepository.findById(taskStateId)
                .orElseThrow(() ->
                    new NotFoundException(
                            String.format("Task State with \"%s\" doesn't exist",
                                    taskStateId)
                    )
                );
    }


}
