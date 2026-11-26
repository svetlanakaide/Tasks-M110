package com.example.demo.repository;

import com.example.demo.entity.UserSkill;
import com.example.demo.entity.UserSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkillRepository
        extends JpaRepository<UserSkill, UserSkillId> {

    List<UserSkill> findAllByUser_Id(Long userId);

    List<UserSkill> findAllBySkill_Id(Long skillId);

    boolean existsByUser_IdAndSkill_Id(Long userId, Long skillId);
}
