package com.polugrudov.nottasktrackerapi.store.repositories;

import com.polugrudov.nottasktrackerapi.store.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
