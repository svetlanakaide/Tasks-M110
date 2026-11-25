package com.example.demo.controller;

import com.example.demo.dto.recommendation.RecommendationResponse;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.RecommendationService; //todo
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // Мои рекомендации (ВАЖНО: объявлен ДО /{recommendationId}, иначе /my будет интерпретирован как ID)

    @GetMapping("/my")
    public ResponseEntity<List<RecommendationResponse>> getMyRecommendations(
            @PathVariable Long taskId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(recommendationService.getMyRecommendations(taskId, userDetails.getUser()));
    }

    // Создать рекомендацию

    @PostMapping
    public ResponseEntity<RecommendationResponse> createRecommendation(
            @PathVariable Long taskId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long suggestedUserId,
            @RequestParam(required = false) String reason) {
        RecommendationResponse created = recommendationService.createRecommendation(
                taskId, userDetails.getUser(), suggestedUserId, reason);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{recommendationId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // Получить рекомендации задачи

    @GetMapping
    public ResponseEntity<List<RecommendationResponse>> getRecommendations(
            @PathVariable Long taskId) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByTaskId(taskId));
    }

    // Получить рекомендацию

    @GetMapping("/{recommendationId}")
    public ResponseEntity<RecommendationResponse> getRecommendation(
            @PathVariable Long taskId,
            @PathVariable Long recommendationId) {
        return ResponseEntity.ok(recommendationService.getRecommendationById(taskId, recommendationId));
    }

    // Принять рекомендацию

    @PatchMapping("/{recommendationId}/accept")
    public ResponseEntity<RecommendationResponse> acceptRecommendation(
            @PathVariable Long taskId,
            @PathVariable Long recommendationId) {
        return ResponseEntity.ok(recommendationService.acceptRecommendation(taskId, recommendationId));
    }

    // Отклонить рекомендацию

    @PatchMapping("/{recommendationId}/reject")
    public ResponseEntity<RecommendationResponse> rejectRecommendation(
            @PathVariable Long taskId,
            @PathVariable Long recommendationId) {
        return ResponseEntity.ok(recommendationService.rejectRecommendation(taskId, recommendationId));
    }
}