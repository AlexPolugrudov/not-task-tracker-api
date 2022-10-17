package com.polugrudov.nottasktrackerapi.api.factories;

import com.polugrudov.nottasktrackerapi.api.dto.TaskDto;
import com.polugrudov.nottasktrackerapi.store.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto(TaskEntity entity) {
        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .description(entity.getDescription())
                .build();
    }
}
