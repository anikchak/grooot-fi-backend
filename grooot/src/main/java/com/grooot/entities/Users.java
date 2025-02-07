package com.grooot.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.grooot.enums.UserSource;
import com.grooot.enums.UserStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @GeneratedValue(generator = "UUID")
    private UUID userUUID;
    private String displayName;
    private String profilePic;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserSource userSource;
    private String externalSourceId;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @CreationTimestamp
    private LocalDateTime userCreatedOn;
    @UpdateTimestamp
    private LocalDateTime userUpdatedOn;
}
