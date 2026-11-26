package com.example.demo.controller;

import com.example.demo.dto.skill.UserSkillRequest;
import com.example.demo.dto.skill.UserSkillResponse;
import com.example.demo.dto.user.ChangePasswordRequest;
import com.example.demo.dto.user.ChangeSystemRoleRequest;
import com.example.demo.dto.user.UpdateUserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.dto.user.UserShortResponse;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.UserSkillService; //todo??
import com.example.demo.service.UserService; //todo
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserSkillService userSkillService;

    public UserController(UserService userService, UserSkillService userSkillService) {
        this.userService = userService;
        this.userSkillService = userSkillService;
    }

    // Список пользователей

    @GetMapping
    public ResponseEntity<List<UserShortResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    // Профиль текущего пользователя

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getMyProfile(userDetails.getUser()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateMyProfile(userDetails.getUser(), request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userDetails.getUser(), request);
        return ResponseEntity.noContent().build();
    }

    // Навыки текущего пользователя

    @GetMapping("/me/skills")
    public ResponseEntity<List<UserSkillResponse>> getMySkills(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userSkillService.getUserSkills(userDetails.getUser().getId()));
    }

    @PutMapping("/me/skills")
    public ResponseEntity<List<UserSkillResponse>> updateMySkills(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody List<UserSkillRequest> requests) {
        return ResponseEntity.ok(userSkillService.updateUserSkills(userDetails.getUser().getId(), requests));
    }

    // Профиль произвольного пользователя

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> changeSystemRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeSystemRoleRequest request) {
        return ResponseEntity.ok(userService.changeSystemRole(id, request));
    }

    // Навыки произвольного пользователя

    @GetMapping("/{id}/skills")
    public ResponseEntity<List<UserSkillResponse>> getUserSkills(@PathVariable Long id) {
        return ResponseEntity.ok(userSkillService.getUserSkills(id));
    }
}