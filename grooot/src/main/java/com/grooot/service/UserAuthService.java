package com.grooot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.grooot.request.UserAuthRequest;
import com.grooot.response.BaseResponse;
import com.grooot.response.UserAuthResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAuthService {
    
    public BaseResponse loginOrSignup(UserAuthRequest request) {
        //validate request
        if ((request.getSsoAccessToken() == null || request.getSsoAccessToken().trim().isEmpty()) && 
        (request.getSsoSource() ==null ||request.getSsoSource().trim().isEmpty())) {
            return userAuthResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request", null);   
        }
        return null;
    }
    private BaseResponse userAuthResponse(int code, String message, Object data) {
        return BaseResponse.builder().code(code).message(message).data(data).build();
    }
}
