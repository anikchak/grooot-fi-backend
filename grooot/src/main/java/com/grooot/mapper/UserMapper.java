package com.grooot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.grooot.dto.UserDetails;
import com.grooot.dto.response.UserDetailsResponse;
import com.grooot.entities.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDetails toDto(Users user);
    Users toEntity(UserDetails userDTO);

    // Mapping limited fields to UserResponse
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "userUUID", source = "userUUID")
    @Mapping(target = "displayName", source = "displayName")
    @Mapping(target = "profilePic", source = "profilePic")
    UserDetailsResponse toResponse(UserDetails userDetails);
}

