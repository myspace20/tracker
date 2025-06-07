package com.task.tracker.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 255)
    @NotBlank(message = "Project name is required")
    @Column(nullable = false, length = 255)
    private String name;

    @Size(min = 2, max = 255)
    @NotBlank(message = "Project description is required")
    @Column(nullable = false, length = 255)
    private String description;

    @Size(min = 2, max = 255)
    @NotBlank(message = "Project deadline is required")
    @Column(nullable = false, length = 255)
    private Date deadline;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Project(String name, String description, Date deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }


    public Project() {

    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
