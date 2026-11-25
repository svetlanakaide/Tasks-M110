package com.example.demo.controller;

import com.example.demo.dto.sprint.CreateSprintRequest;
import com.example.demo.dto.sprint.SprintResponse;
import com.example.demo.dto.sprint.UpdateSprintRequest;
import com.example.demo.dto.task.TaskResponse;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.SprintService; //todo
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/sprints")
public class SprintController {

    private final SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    // Создать спринт

    @PostMapping
    public ResponseEntity<SprintResponse> createSprint(
            @PathVariable Long projectId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateSprintRequest request) {
        SprintResponse created = sprintService.createSprint(projectId, userDetails.getUser(), request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{sprintId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // Получить спринты проекта

    @GetMapping
    public ResponseEntity<List<SprintResponse>> getSprints(@PathVariable Long projectId) {
        return ResponseEntity.ok(sprintService.getSprintsByProjectId(projectId));
    }

    // Получить конкретный спринт

    @GetMapping("/{sprintId}")
    public ResponseEntity<SprintResponse> getSprint(
            @PathVariable Long projectId,
            @PathVariable Long sprintId) {
        return ResponseEntity.ok(sprintService.getSprintById(projectId, sprintId));
    }

    // Изменить спринт

    @PutMapping("/{sprintId}")
    public ResponseEntity<SprintResponse> updateSprint(
            @PathVariable Long projectId,
            @PathVariable Long sprintId,
            @Valid @RequestBody UpdateSprintRequest request) {
        return ResponseEntity.ok(sprintService.updateSprint(projectId, sprintId, request));
    }

    // Изменить статус (переиспользуется UpdateSprintRequest)

    @PatchMapping("/{sprintId}/status")
    public ResponseEntity<SprintResponse> updateSprintStatus(
            @PathVariable Long projectId,
            @PathVariable Long sprintId,
            @Valid @RequestBody UpdateSprintRequest request) {
        return ResponseEntity.ok(sprintService.updateSprintStatus(projectId, sprintId, request));
    }

    // Удалить спринт

    @DeleteMapping("/{sprintId}")
    public ResponseEntity<Void> deleteSprint(
            @PathVariable Long projectId,
            @PathVariable Long sprintId) {
        sprintService.deleteSprint(projectId, sprintId);
        return ResponseEntity.noContent().build();
    }

    // Задачи спринта

    @GetMapping("/{sprintId}/tasks")
    public ResponseEntity<List<TaskResponse>> getSprintTasks(
            @PathVariable Long projectId,
            @PathVariable Long sprintId) {
        return ResponseEntity.ok(sprintService.getSprintTasks(projectId, sprintId));
    }
}