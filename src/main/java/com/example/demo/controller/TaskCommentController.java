package com.example.demo.controller;

import com.example.demo.dto.comment.CommentResponse;
import com.example.demo.dto.comment.CreateCommentRequest;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.TaskCommentService; //todo
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    public TaskCommentController(TaskCommentService taskCommentService) {
        this.taskCommentService = taskCommentService;
    }

    // Добавить комментарий

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long taskId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateCommentRequest request) {
        CommentResponse created = taskCommentService.addComment(taskId, userDetails.getUser(), request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // Получить комментарии задачи

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskCommentService.getCommentsByTaskId(taskId));
    }

    // Получить комментарий

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId) {
        return ResponseEntity.ok(taskCommentService.getCommentById(taskId, commentId));
    }

    // Изменить комментарий
    // Переиспользуется CreateCommentRequest: единственное поле text с @NotBlank.

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(
                taskCommentService.updateComment(taskId, commentId, userDetails.getUser(), request));
    }

    // Удалить комментарий

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskCommentService.deleteComment(taskId, commentId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}