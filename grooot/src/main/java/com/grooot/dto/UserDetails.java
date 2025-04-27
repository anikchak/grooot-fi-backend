package com.grooot.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.grooot.enums.UserSource;
import com.grooot.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDetails {
    public UserDetails(String extId, String displayName, String email, String profilePic, UserSource userSource) {
        this.externalSourceId = extId;
        this.displayName = displayName;
        this.email = email;
        this.profilePic = profilePic;
        this.userSource = userSource;    
    }

    private Long userId;
    private UUID userUUID;
    private String displayName;
    private String profilePic;
    private String email;
    private UserSource userSource;
    private String externalSourceId;
    private UserStatus userStatus;
    private String accessToken;
    private LocalDateTime userCreatedOn;
    private LocalDateTime userUpdatedOn;
}
