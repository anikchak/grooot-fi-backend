package com.grooot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.grooot.dto.UserTokenDetails;
import com.grooot.entities.UserTokens;

@Mapper(componentModel = "spring")
public interface UserTokensMapper {
    UserTokensMapper INSTANCE = Mappers.getMapper(UserTokensMapper.class);

    UserTokenDetails toDto(UserTokens user);
    UserTokens toEntity(UserTokenDetails userDTO);
    
}
