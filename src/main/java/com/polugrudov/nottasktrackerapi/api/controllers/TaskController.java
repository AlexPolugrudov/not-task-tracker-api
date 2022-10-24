package com.polugrudov.nottasktrackerapi.api.controllers;


import com.polugrudov.nottasktrackerapi.api.controllers.helpers.ControllerHelper;
import com.polugrudov.nottasktrackerapi.api.dto.AskDto;
import com.polugrudov.nottasktrackerapi.api.dto.TaskDto;
import com.polugrudov.nottasktrackerapi.api.exceptions.BadRequestException;
import com.polugrudov.nottasktrackerapi.api.exceptions.NotFoundException;
import com.polugrudov.nottasktrackerapi.api.factories.TaskDtoFactory;
import com.polugrudov.nottasktrackerapi.store.entities.ProjectEntity;
import com.polugrudov.nottasktrackerapi.store.entities.TaskEntity;
import com.polugrudov.nottasktrackerapi.store.entities.TaskStateEntity;
import com.polugrudov.nottasktrackerapi.store.repositories.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class TaskController {

        TaskRepository taskRepository;

        TaskDtoFactory taskDtoFactory;

        ControllerHelper controllerHelper;

        private static final String GET_TASK = "/api/projects/{project_id}/task-states/{task_state_id}/tasks";
        private static final String CREATE_TASK = "/api/projects/{project_id}/task-states/{task_state_id}/tasks";

        private static final String UPDATE_TASK = "/api/projects/task-states/tasks/{task_id}";

        private static final String DELETE_TAK = "/api/projects/task-states/tasks/{task_id}";

        @GetMapping(GET_TASK)
        public List<TaskDto> getTaskStates(@PathVariable(name = "project_id") Long projectId,
                                           @PathVariable(name = "task_state_id") Long taskStateId) {

                ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

                TaskStateEntity taskStateEntity = controllerHelper.getTaskStatesOrThrowException(taskStateId);

                if (!project.getId().equals(taskStateEntity.getProject().getId()))
                        throw new BadRequestException("This project does not have such Task State");

                return taskStateEntity.getTasks()
                        .stream()
                        .map(taskDtoFactory::makeTaskDto)
                        .collect(Collectors.toList());
        }

        @PostMapping(CREATE_TASK)
        public TaskDto createTask(@PathVariable(name = "project_id") Long projectId,
                                  @PathVariable(name = "task_state_id") Long taskStateId,
                                  @RequestParam(name = "task_name") String taskName,
                                  @RequestParam(name = "description") String description) {

                if (taskName.trim().isEmpty())
                        throw new BadRequestException("Task name can't be empty");

                if (description.trim().isEmpty())
                        throw new BadRequestException("Description can't be empty");

                ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

                TaskStateEntity taskStateEntity = controllerHelper.getTaskStatesOrThrowException(taskStateId);

                if (!project.getId().equals(taskStateEntity.getProject().getId()))
                        throw new BadRequestException("This project does not have such Task State");

                for (TaskEntity task : taskStateEntity.getTasks()) {

                        if (task.getName().equalsIgnoreCase(taskName))
                                throw new BadRequestException(String.format("Task \"%s\" already exist.", taskName));
                }

                TaskEntity task = taskRepository.saveAndFlush(
                        TaskEntity.builder()
                                .name(taskName)
                                .taskState(taskStateEntity)
                                .description(description)
                                .build()
                );

                final TaskEntity savedTask = taskRepository.saveAndFlush(task);

                return taskDtoFactory.makeTaskDto(savedTask);
        }

        @PatchMapping(UPDATE_TASK)
        public TaskDto updateTask(@PathVariable(name = "task_id") Long taskId,
                                  @RequestParam(name = "task_name") String taskName,
                                  @RequestParam(name = "description", required = false) Optional<String> description) {

                if (taskName.isEmpty())
                        throw new BadRequestException("Task name can't be empty");

                TaskEntity task = getTaskOrThrowException(taskId);

                taskRepository
                        .findTaskEntityByTaskStateIdAndNameIgnoreCase(
                                task.getTaskState().getId(),
                                taskName
                        )
                        .filter(anotherTask -> !anotherTask.getId().equals(taskId))
                        .ifPresent(anotherTask -> {
                                throw new BadRequestException(String.format("Task \"%s\" already exists.", taskName));
                        });

                task.setName(taskName);

                if (description.isPresent()) {
                    if (!description.equals(task.getDescription()))
                            task.setDescription(String.valueOf(description));
                }

                task = taskRepository.saveAndFlush(task);

                return taskDtoFactory.makeTaskDto(task);
        }

        @DeleteMapping(DELETE_TAK)
        public AskDto deleteTask(@PathVariable(name = "task_id") Long taskId) {

                TaskEntity changeTask = getTaskOrThrowException(taskId);

                taskRepository.delete(changeTask);

                return AskDto.builder()
                        .answer(true)
                        .build();
        }

        private TaskEntity getTaskOrThrowException(Long taskId) {

                return taskRepository
                        .findById(taskId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        String.format("Task state with \"%s\" id doesn't exist", taskId)
                                )
                        );
        }


}
