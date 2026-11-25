package com.example.demo.repository;

import com.example.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByOwner_Id(Long ownerId);

    List<Project> findAllByNameContainingIgnoreCase(String name);

    boolean existsByNameAndOwner_Id(String name, Long ownerId);
}
