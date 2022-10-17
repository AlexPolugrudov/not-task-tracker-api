package com.polugrudov.nottasktrackerapi.store.repositories;

import com.polugrudov.nottasktrackerapi.store.entities.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
}
