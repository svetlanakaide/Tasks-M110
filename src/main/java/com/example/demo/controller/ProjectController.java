package com.example.demo.controller;

import com.example.demo.dto.project.CreateProjectRequest;
import com.example.demo.dto.project.ProjectResponse;
import com.example.demo.dto.project.UpdateProjectRequest;
import com.example.demo.dto.sprint.SprintResponse;
import com.example.demo.dto.task.TaskResponse;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.ProjectService; //todo
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Создать проект

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateProjectRequest request) {
        ProjectResponse created = projectService.createProject(userDetails.getUser(), request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // Получить доступные пользователю проекты

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAvailableProjects(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(projectService.getAvailableProjects(userDetails.getUser()));
    }

    // Получить проект

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // Изменить проект

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    // Удалить проект

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // Задачи проекта

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskResponse>> getProjectTasks(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectTasks(id));
    }

    // Спринты проекта

    @GetMapping("/{id}/sprints")
    public ResponseEntity<List<SprintResponse>> getProjectSprints(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectSprints(id));
    }
}