package com.example.demo.dto.project;

import com.example.demo.dto.user.UserShortResponse;
import com.example.demo.enums.ProjectRole;

public record ProjectMemberResponse(

        Long projectId,

        UserShortResponse user,

        ProjectRole role

) {
}