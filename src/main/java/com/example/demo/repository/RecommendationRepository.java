package com.example.demo.repository;

import com.example.demo.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository
        extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findAllByTask_Id(Long taskId);

    List<Recommendation> findAllBySuggestedUser_Id(Long userId);

    List<Recommendation> findAllBySuggestedUser_IdAndAccepted(
            Long userId,
            Boolean accepted
    );
}
