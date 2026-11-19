package com.example.demo.repository;

import com.example.demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProject_Id(Long projectId);

    List<Task> findAllBySprint_Id(Long sprintId);

    List<Task> findAllByAssignee_Id(Long userId);

    List<Task> findAllByReporter_Id(Long userId);

    List<Task> findAllByParent_Id(Long parentId);

    List<Task> findAllByProject_IdAndStatus(
            Long projectId,
            String status
    );

    List<Task> findAllByProject_IdAndAssignee_Id(
            Long projectId,
            Long userId
    );
}
