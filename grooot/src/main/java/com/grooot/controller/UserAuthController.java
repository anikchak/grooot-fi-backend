package com.grooot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.grooot.request.UserAuthRequest;
import com.grooot.response.BaseResponse;
import com.grooot.service.UserAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    @Autowired
    UserAuthService userAuthService;
    
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<BaseResponse> loginOrSignup(@RequestBody UserAuthRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        BaseResponse userDetails = userAuthService.loginOrSignup(request,authHeader);
        return ResponseEntity.status(userDetails.getCode()).body(userDetails);
    }
}
