package com.polugrudov.nottasktrackerapi.store.repository;

import com.polugrudov.nottasktrackerapi.store.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findTaskEntityByTaskStateIdAndNameIgnoreCase (Long taskStateId, String taskName);
}
