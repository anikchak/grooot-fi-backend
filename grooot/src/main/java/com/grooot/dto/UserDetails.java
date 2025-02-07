package com.grooot.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.grooot.enums.UserSource;
import com.grooot.enums.UserStatus;


import lombok.Data;

@Data
public class UserDetails {
    private Long userId;
    private UUID userUUID;
    private String displayName;
    private String profilePic;
    private String email;
    private UserSource userSource;
    private String externalSourceId;
    private UserStatus userStatus;
    private LocalDateTime userCreatedOn;
    private LocalDateTime userUpdatedOn;
}
