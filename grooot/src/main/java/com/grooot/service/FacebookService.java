package com.grooot.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.ParameterizedTypeReference;

import com.grooot.dto.UserDetails;
import com.grooot.enums.UserSource;
import com.grooot.exception.FacebookUserDataException;

import reactor.core.publisher.Mono;

@Service
public class FacebookService {

    private final WebClient webClient;

    public FacebookService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://graph.facebook.com").build();
    }

    
    public UserDetails getFacebookUserDetails(String accessToken) throws FacebookUserDataException {
        Map<String, Object> facebookUserdetails = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/me")
                        .queryParam("fields", "id,name,email,picture")
                        .queryParam("access_token", accessToken)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        return Mono.just(Map.of("error", "Invalid or expired access token."));
                    } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                        return Mono.just(Map.of("error", "Missing permissions. Please grant required Facebook permissions."));
                    }
                    return Mono.just(Map.of("error", "An unexpected error occurred while fetching data from Facebook."));
                })
                .block();

        // Convert Map to UserDetails
        return extractFacebookData(facebookUserdetails);
       
    }

    public UserDetails extractFacebookData(Map<String, Object> userData) throws FacebookUserDataException {
        //Check if error occurred
        if (userData == null) {
            throw new FacebookUserDataException("Error occurred because facebook userData is null");
        }
        if (userData.containsKey("error")) {
            String errorMessage = (String) userData.get("error");
            // Handle error
            throw new FacebookUserDataException("Error occurred while fetching facebook userdata because "+ errorMessage); 
        }
        String id = (String) userData.get("id");
        String name = (String) userData.get("name");
        String email = (String) userData.get("email");
    
        // Extract Profile Picture URL
        String profilePicUrl = "";
        if (userData.containsKey("picture")) {
            Map<String, Object> picture = (Map<String, Object>) userData.get("picture");
            Map<String, Object> pictureData = (Map<String, Object>) picture.get("data");
            profilePicUrl = (String) pictureData.get("url");
        }

        return new UserDetails(id, name, email, profilePicUrl,UserSource.FB);
    }
    
}
