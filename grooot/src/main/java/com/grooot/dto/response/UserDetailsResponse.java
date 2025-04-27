package com.grooot.dto.response;
import java.util.UUID;

import lombok.Data;


@Data
public class UserDetailsResponse {
    private Long userId;
    private UUID userUUID;
    private String displayName;
    private String profilePic;
    private String accessToken;
}


