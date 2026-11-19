package com.example.demo.repository;

import com.example.demo.entity.Sprint;
import com.example.demo.enums.SprintStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    List<Sprint> findAllByProject_Id(Long projectId);

    List<Sprint> findAllByProject_IdOrderByStartDateAsc(Long projectId);

    List<Sprint> findAllByProject_IdAndStatus(
            Long projectId,
            SprintStatus status
    );
}
