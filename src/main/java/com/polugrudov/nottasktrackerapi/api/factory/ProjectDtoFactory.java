package com.polugrudov.nottasktrackerapi.api.factory;

import com.polugrudov.nottasktrackerapi.api.dto.ProjectDto;
import com.polugrudov.nottasktrackerapi.store.entity.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {

    public ProjectDto makeProjectDto(ProjectEntity entity) {
        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
