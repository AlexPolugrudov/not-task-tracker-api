package com.polugrudov.nottasktrackerapi.api.controllers;

import com.polugrudov.nottasktrackerapi.api.dto.AskDto;
import com.polugrudov.nottasktrackerapi.api.dto.ProjectDto;
import com.polugrudov.nottasktrackerapi.api.exceptions.BadRequestException;
import com.polugrudov.nottasktrackerapi.api.exceptions.NotFoundException;
import com.polugrudov.nottasktrackerapi.api.factories.ProjectDtoFactory;
import com.polugrudov.nottasktrackerapi.store.entities.ProjectEntity;
import com.polugrudov.nottasktrackerapi.store.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProjectController {

    ProjectDtoFactory projectDtoFactory;

    ProjectRepository projectRepository;

    private static final String FETCH_PROJECT = "/api/projects";
    private static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";
    private static final String DELETE_PROJECT = "/api/projects/{project_id}";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDto> fetchProject(
            @RequestParam(value = "prefix_nname", required = false)Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartingWithIgnoreCase)
                .orElseGet(projectRepository::streamAll);

        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDto createOrUpdateProject(
            @RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName) {

        optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());

        boolean isCreate = !optionalProjectId.isPresent();

        if (isCreate && !optionalProjectName.isPresent()) {
            throw new BadRequestException("Project name can't be empty.");
        }

        final ProjectEntity project = optionalProjectId
                .map(this::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());


        optionalProjectName
                .ifPresent(projectName -> {

                    projectRepository
                            .findByName(projectName)
                            .filter(anotherName -> !Objects.equals(anotherName.getId(), project.getId()))
                            .ifPresent(anotherProject -> {
                                throw new BadRequestException(String.format("Project \"%s\" already exist.", projectName));
                            });
                    project.setName(projectName);
                });

        final ProjectEntity savedProject = projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AskDto deleteProject(
            @PathVariable("project_id") Long project_id) {

        ProjectEntity project = getProjectOrThrowException(project_id);

        projectRepository.deleteById(project_id);

        return AskDto.makeDefault(true);
    }

    private ProjectEntity getProjectOrThrowException(Long project_id) {
        return projectRepository.findById(project_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" doesn't exist",
                                        project_id)
                        )
                );
    }
}
