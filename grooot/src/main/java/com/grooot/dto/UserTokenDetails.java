package com.grooot.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserTokenDetails {
    private Long id;
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime userCreatedOn;
    private LocalDateTime userUpdatedOn;
}
