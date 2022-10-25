package com.polugrudov.nottasktrackerapi.store.repository;

import com.polugrudov.nottasktrackerapi.store.entity.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {

    Optional<TaskStateEntity> findTaskStateEntityByProjectIdAndNameContainsIgnoreCase (Long projectId, String taskStateName);

}
