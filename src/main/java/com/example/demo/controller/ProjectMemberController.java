package com.example.demo.controller;

import com.example.demo.dto.project.AddProjectMemberRequest;
import com.example.demo.dto.project.ProjectMemberResponse;
import com.example.demo.dto.project.UpdateProjectMemberRoleRequest;
import com.example.demo.service.ProjectMemberService; //todo
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    // Добавить участника

    @PostMapping
    public ResponseEntity<ProjectMemberResponse> addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody AddProjectMemberRequest request) {
        ProjectMemberResponse created = projectMemberService.addMember(projectId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(request.userId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // Получить список участников

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getMembers(
            @PathVariable Long projectId) {
        return ResponseEntity.ok(projectMemberService.getMembers(projectId));
    }

    // Получить участника

    @GetMapping("/{userId}")
    public ResponseEntity<ProjectMemberResponse> getMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(projectMemberService.getMember(projectId, userId));
    }

    // Изменить роль участника

    @PutMapping("/{userId}/role")
    public ResponseEntity<ProjectMemberResponse> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProjectMemberRoleRequest request) {
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId, userId, request));
    }

    // Удалить участника

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectMemberService.removeMember(projectId, userId);
        return ResponseEntity.noContent().build();
    }
}