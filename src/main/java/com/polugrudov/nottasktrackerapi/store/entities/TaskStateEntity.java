package com.polugrudov.nottasktrackerapi.store.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_state")
public class TaskStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(unique = true)
    String name;

    String ordinal;

    @Builder.Default
    Instant createdAt = Instant.now();

    @OneToMany
    @JoinColumn(name = "task_state_id", referencedColumnName = "id")
    @Builder.Default
    List<TaskEntity> tasks = new ArrayList<>();
}
