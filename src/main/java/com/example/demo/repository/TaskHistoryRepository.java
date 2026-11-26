package com.example.demo.repository;

import com.example.demo.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskHistoryRepository
        extends JpaRepository<TaskHistory, Long> {

    List<TaskHistory> findAllByTask_IdOrderByUpdatedAtDesc(Long taskId);

    List<TaskHistory> findAllByUser_Id(Long userId);
}
