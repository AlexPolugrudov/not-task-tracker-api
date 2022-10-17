package com.polugrudov.nottasktrackerapi.api.factories;

import com.polugrudov.nottasktrackerapi.api.dto.ProjectDto;
import com.polugrudov.nottasktrackerapi.api.dto.TaskStateDto;
import com.polugrudov.nottasktrackerapi.store.entities.ProjectEntity;
import com.polugrudov.nottasktrackerapi.store.entities.TaskStateEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskStateDtoFactory {

    public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {
        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ordinal(entity.getOrdinal())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
