package com.grooot.grooot;

import java.util.Collections;

import org.checkerframework.checker.units.qual.s;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@SpringBootApplication
public class GroootApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroootApplication.class, args);
		try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
				new NetHttpTransport(),
				JacksonFactory.getDefaultInstance()
				).setAudience(Collections.singletonList("1064920344841-l1cfu2auhm2lkvlgs63u9muan9arjg5n.apps.googleusercontent.com"))
            .build();

            // Verify the ID token
            GoogleIdToken idToken = verifier.verify("eyJhbGciOiJSUzI1NiIsImtpZCI6IjYzMzdiZTYzNjRmMzgyNDAwOGQwZTkwMDNmNTBiYjZiNDNkNWE5YzYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMDY0OTIwMzQ0ODQxLW83ZmZrbXA2N2Nta28wN2FiNmZtMW91dWlwMHZhdWh1LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA2NDkyMDM0NDg0MS1sMWNmdTJhdWhtMmxrdmxnczYzdTltdWFuOWFyamc1bi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwNDMwMDI2OTAzMDcxODIxNTkwMiIsImVtYWlsIjoiYW5pa2NoYWtAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJhbmlrZXQgY2hha3JhYm9ydHkiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUNnOG9jSmE0SEdidVRNM3JnMmRnQzByOVBZbWF4aXR0ZmhyZUwyVk4yWXBhaFc3LVQ3Z2NnPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6ImFuaWtldCIsImZhbWlseV9uYW1lIjoiY2hha3JhYm9ydHkiLCJpYXQiOjE3Mzc4OTIwNDIsImV4cCI6MTczNzg5NTY0Mn0.Idf9VNDkgZ9MopobH9vL-NDY2yNcNCL5mzQzpCnFoV4L82aVVRXa8VCOLUlq5EyqrWTIX7e3BtEz7dURuQ8_q4oOSzSvOYxjEsDzLtHz-Y-a-4GmEjdq2dEeV04dWpKfrXtB9opAtNKn-TGl6i9TVRMNpymKG5ShK8he7xhiifSdhVKth09SjsE7KF48JIeZNs47AK4_OLy4r9heo7iel3CRKv9_sh76mVig5aNJ7L0v-c");
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Extract user details from the token payload
                String userId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                // Check if user exists in your database (pseudo code)
                // User user = userRepository.findByEmail(email);
                // if (user == null) {
                //     user = new User(email, name, pictureUrl);
                //     userRepository.save(user);
                // }

                System.out.println("here");;
            } else {
                System.out.println("Invalid ID token.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
