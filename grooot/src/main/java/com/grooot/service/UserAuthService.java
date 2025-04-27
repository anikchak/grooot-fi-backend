package com.grooot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.grooot.constants.CommonConstants;
import com.grooot.dto.UserDetails;
import com.grooot.dto.UserTokenDetails;
import com.grooot.dto.response.UserDetailsResponse;
import com.grooot.exception.FacebookUserDataException;
import com.grooot.exception.UserCreationFailed;
import com.grooot.exception.UserNotFoundException;
import com.grooot.exception.UserTokenNotFoundException;
import com.grooot.exception.UserTokenNotUpdatedException;
import com.grooot.mapper.UserMapper;
import com.grooot.repo.action.UserAction;
import com.grooot.request.UserAuthRequest;
import com.grooot.response.BaseResponse;
import com.grooot.utility.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    @Autowired
    FacebookService facebookService;
    @Autowired
    UserAction userAction;
    @Autowired
    JwtUtil jwtUtil;

    UserMapper userMapper;
    
    public BaseResponse loginOrSignup(UserAuthRequest request, String authHeader) {
        //Validate request, If request validation fails, return error
        String socialToken = authHeader.replace("Bearer ", "");
        boolean isValid = validateRequest(request, authHeader);
        if(!isValid){
            return userAuthResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request", null);   
        }
        
        //fetch user details from social login based on provider
        try {
            UserDetails userDetails = fetchUserDetails(request,socialToken);
            UserDetailsResponse userDetailsResponse = userMapper.toResponse(userDetails);
            return userAuthResponse(HttpStatus.OK.value(), "User details fetched", userDetailsResponse);

        } catch (FacebookUserDataException fbe) {
            return userAuthResponse(HttpStatus.NOT_FOUND.value(), fbe.getMessage(), null);
        } catch (UserTokenNotFoundException | UserTokenNotUpdatedException | UserCreationFailed e) {
            return userAuthResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        } 
    }
    private UserDetails fetchUserDetails(UserAuthRequest request,String socialToken) throws FacebookUserDataException, UserTokenNotFoundException, UserTokenNotUpdatedException, UserCreationFailed {
        //fetch user details from social login based on source
        UserDetails userDetails = null;
        if (request.getSsoProvider().equalsIgnoreCase(CommonConstants.SSO_PROVIDER_GOOGLE)) {
            userDetails = fetchUserDetailsFromGoogle(socialToken);
        } else if (request.getSsoProvider().equalsIgnoreCase(CommonConstants.SSO_PROVIDER_FACEBOOK)) {
            try{
                userDetails = fetchUserDetailsFromFacebook(socialToken);
            } catch (FacebookUserDataException fbe) {
                throw new FacebookUserDataException("Failed to fetch user details from Facebook");
            }
        }
         userDetails = checkOrCreateUser(userDetails);
        return userDetails;
    }
    public UserTokenDetails fetchUserAccessToken(Long userId, String userUUID) throws UserTokenNotFoundException, UserTokenNotUpdatedException{
        //fetch User access token for the user
        UserTokenDetails userTokenDetails = userAction.fetchUserTokenDetailsByUserId(userId);
        //check if the token has not expired
        boolean isAccessTokenExpired = jwtUtil.isTokenExpired(userTokenDetails.getAccessToken());
        boolean isRefreshTokenExpired = jwtUtil.isTokenExpired(userTokenDetails.getRefreshToken());
        if(isAccessTokenExpired && !isRefreshTokenExpired){
            //generate new access token
            String newAccessToken = jwtUtil.generateAccessToken(userUUID);
            userTokenDetails.setAccessToken(newAccessToken);
            userAction.updateUserTokenDetails(userTokenDetails);
        } else if(isAccessTokenExpired && isRefreshTokenExpired){
            //generate new refresh token and access token
            String newAccessToken = jwtUtil.generateAccessToken(userUUID);
            String newRefreshToken = jwtUtil.generateRefreshToken(userUUID);
            userTokenDetails.setAccessToken(newAccessToken);
            userTokenDetails.setRefreshToken(newRefreshToken);
            userAction.updateUserTokenDetails(userTokenDetails);
        }

        return userTokenDetails;
    }
    private boolean validateRequest(UserAuthRequest request, String socialToken) {
        //validate request
        if (request.getSsoProvider() == null || request.getSsoProvider().trim().isEmpty()) {
            return false;
        }
        if (request.getSsoProvider().equalsIgnoreCase(CommonConstants.SSO_PROVIDER_GOOGLE) && socialToken == null) {
            return false;
        }
        if (request.getSsoProvider().equalsIgnoreCase(CommonConstants.SSO_PROVIDER_FACEBOOK) && socialToken == null) {
            return false;
        }
        return true;
    }
    private UserDetails fetchUserDetailsFromGoogle(String socialToken) {
        //fetch user details from google
        
        return null;
    }
    private UserDetails fetchUserDetailsFromFacebook(String socialToken) throws FacebookUserDataException {
        //fetch user details from facebook
        
            UserDetails facebookUserDetails = facebookService.getFacebookUserDetails(socialToken);
            return facebookUserDetails;
    
    }
    private UserDetails checkOrCreateUser(UserDetails userDetails) throws UserCreationFailed, UserTokenNotFoundException, UserTokenNotUpdatedException{
        //Check if this is a new user or an already existing user
        try {
            //Step 1: Check if user exists in the database
                userDetails = checkIfUserExistsForExtSrcId(userDetails.getExternalSourceId());
            //Step 1.1: If user exists, return access token
                UserTokenDetails userTokenDetails = fetchUserAccessToken(userDetails.getUserId(), userDetails.getUserUUID().toString());
            // Step 1.2: Return user details along with access token
                userDetails.setAccessToken(userTokenDetails.getAccessToken());
            } catch (UserNotFoundException e) {
                //Step 2: If user does not exist, create a new user
                userDetails = createNewUser(userDetails);
            } catch (UserTokenNotFoundException userTokenNotFoundException){
                throw userTokenNotFoundException;
            } catch (UserTokenNotUpdatedException userTokenNotUpdatedException){
                throw userTokenNotUpdatedException;
            }
        return userDetails;
    }
    private UserDetails checkIfUserExistsForExtSrcId(String extSrcId) throws UserNotFoundException{
        return userAction.fetchUserDetailsForExternalSourceId(extSrcId);
    }
    private UserDetails createNewUser(UserDetails userDetails) throws UserCreationFailed{
        userDetails = userAction.saveUser(userDetails);
        return userDetails;

    }

    private BaseResponse userAuthResponse(int code, String message, Object data) {
        return BaseResponse.builder().code(code).message(message).data(data).build();
    }
}
