package com.grooot.request;

import lombok.Data;

@Data
public class UserAuthRequest {
  private String ssoAccessToken;
  private String ssoSource;  
}
