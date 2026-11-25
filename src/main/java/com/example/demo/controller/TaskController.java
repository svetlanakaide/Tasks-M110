package com.example.demo.controller;

import com.example.demo.dto.task.AssignTaskRequest;
import com.example.demo.dto.task.CreateTaskRequest;
import com.example.demo.dto.task.MoveTaskToSprintRequest;
import com.example.demo.dto.task.TaskHistoryResponse;
import com.example.demo.dto.task.TaskResponse;
import com.example.demo.dto.task.TaskShortResponse;
import com.example.demo.dto.task.TaskStatusUpdateRequest;
import com.example.demo.dto.task.UpdateTaskRequest;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.TaskService; //todo
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Мои задачи (ВАЖНО: объявлен ДО /{taskId}, иначе /my будет интерпретирован как taskId)

    @GetMapping("/my")
    public ResponseEntity<List<TaskShortResponse>> getMyTasks(
            @PathVariable Long projectId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(taskService.getMyTasks(projectId, userDetails.getUser()));
    }

    // Создать задачу

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateTaskRequest request) {
        TaskResponse created = taskService.createTask(projectId, userDetails.getUser(), request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{taskId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // Получить задачи проекта

    @GetMapping
    public ResponseEntity<List<TaskShortResponse>> getProjectTasks(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getProjectTasks(projectId));
    }

    // Получить задачy

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(projectId, taskId));
    }

    // Изменить задачу

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(projectId, taskId, request));
    }

    // Изменить статус

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskStatusUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateTaskStatus(projectId, taskId, request));
    }

    // Назначить исполнителя

    @PatchMapping("/{taskId}/assignee")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody AssignTaskRequest request) {
        return ResponseEntity.ok(taskService.assignTask(projectId, taskId, request));
    }

    // Переместить в спринт

    @PatchMapping("/{taskId}/sprint")
    public ResponseEntity<TaskResponse> moveTaskToSprint(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody MoveTaskToSprintRequest request) {
        return ResponseEntity.ok(taskService.moveTaskToSprint(projectId, taskId, request));
    }

    // Изменить приоритет (переиспользуется UpdateTaskRequest)

    @PatchMapping("/{taskId}/priority")
    public ResponseEntity<TaskResponse> updateTaskPriority(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTaskPriority(projectId, taskId, request));
    }

    // Удалить задачу

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        taskService.deleteTask(projectId, taskId);
        return ResponseEntity.noContent().build();
    }

    // История изменений

    @GetMapping("/{taskId}/history")
    public ResponseEntity<List<TaskHistoryResponse>> getTaskHistory(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskHistory(projectId, taskId));
    }
}