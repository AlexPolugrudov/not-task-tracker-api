package com.polugrudov.nottasktrackerapi.store.repositories;

import com.polugrudov.nottasktrackerapi.store.entities.TaskEntity;
import com.polugrudov.nottasktrackerapi.store.entities.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findTaskEntityByTaskStateIdAndNameIgnoreCase (Long taskStateId, String taskName);
}
