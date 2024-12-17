package org.todoer.todoer.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String hashedPassword;

    private AuditMetadata auditMetadata;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "owner_id")
    private Set<Project> projects;

    @OneToMany(mappedBy = "assignedTo")
    @Builder.Default
    private Set<Task> assignedTasks = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @Builder.Default
    private Set<Task> createdTasks = new HashSet<>();
}
