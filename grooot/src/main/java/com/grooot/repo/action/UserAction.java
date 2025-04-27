package com.grooot.repo.action;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.grooot.dto.UserDetails;
import com.grooot.dto.UserTokenDetails;
import com.grooot.entities.UserTokens;
import com.grooot.entities.Users;
import com.grooot.exception.UserCreationFailed;
import com.grooot.exception.UserNotFoundException;
import com.grooot.exception.UserTokenNotFoundException;
import com.grooot.exception.UserTokenNotUpdatedException;
import com.grooot.mapper.UserMapper;
import com.grooot.mapper.UserTokensMapper;
import com.grooot.repo.UserRepository;
import com.grooot.repo.UserTokensRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAction{
    @Autowired
    private final UserRepository usersRepo;
    @Autowired
    private final UserTokensRepository userTokensRepo;
    UserMapper userMapper;
    UserTokensMapper userTokensMapper;
    
    public UserDetails fetchUserDetailsForExternalSourceId(String extSrcId) 
    throws UserNotFoundException {
        Optional<Users> user = usersRepo.fetchByExternalSrcId(extSrcId);
        if(user.isPresent()) {
            return userMapper.toDto(user.get());
        } else {
            throw new UserNotFoundException("User not found with external source id: " + extSrcId);
        }
    }

    public UserDetails saveUser(UserDetails userDetails) throws UserCreationFailed {
        try{
        Users usr = userMapper.toEntity(userDetails);
        Users newUser = usersRepo.save(usr);
        return userMapper.toDto(newUser);
        }catch(Exception exp){
            throw new UserCreationFailed("Failed to create new user with externalId ="+userDetails.getExternalSourceId());
        }
    }

    public UserTokenDetails saveUserTokenDetails(UserTokenDetails userTokenDetails) {
        UserTokens userTokens = userTokensMapper.toEntity(userTokenDetails);
        userTokens = userTokensRepo.save(userTokens);
        return userTokensMapper.toDto(userTokens);
    }

    public UserTokenDetails fetchUserTokenDetailsByUserId(Long userId) throws UserTokenNotFoundException {
        Optional<UserTokens> userTokens = userTokensRepo.fetchByUserId(userId);
        if(userTokens.isPresent()) {
            return userTokensMapper.toDto(userTokens.get());
        } else {
            throw new UserTokenNotFoundException("User token not found for user with id: " + userId);
        }
    }

    public UserTokenDetails updateUserTokenDetails(UserTokenDetails userTokenDetails) throws UserTokenNotUpdatedException {
        int updateCount = userTokensRepo.updateAccessAndRefreshTokenForUser(
            userTokenDetails.getAccessToken(), 
            userTokenDetails.getRefreshToken(), 
            userTokenDetails.getUserId());
        if(updateCount ==  1){
            return userTokenDetails;
        }
        else{
            throw new UserTokenNotUpdatedException("User token for user with id: " + userTokenDetails.getUserId()+ "was not updated");
        }
    }
}