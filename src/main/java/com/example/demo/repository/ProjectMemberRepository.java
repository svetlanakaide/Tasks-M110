package com.example.demo.repository;

import com.example.demo.entity.ProjectMember;
import com.example.demo.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository
        extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findAllByProject_Id(Long projectId);

    List<ProjectMember> findAllByUser_Id(Long userId);

    boolean existsByProject_IdAndUser_Id(
            Long projectId,
            Long userId
    );
}
