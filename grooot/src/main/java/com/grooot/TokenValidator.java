package com.grooot;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class TokenValidator {

    private static final String CLIENT_ID = "1064920344841-o7ffkmp67cmko07ab6fm1ouuip0vauhu.apps.googleusercontent.com"; 

    public static void main(String[] args) {
        String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjYzMzdiZTYzNjRmMzgyNDAwOGQwZTkwMDNmNTBiYjZiNDNkNWE5YzYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMDY0OTIwMzQ0ODQxLW83ZmZrbXA2N2Nta28wN2FiNmZtMW91dWlwMHZhdWh1LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA2NDkyMDM0NDg0MS1sMWNmdTJhdWhtMmxrdmxnczYzdTltdWFuOWFyamc1bi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwNDMwMDI2OTAzMDcxODIxNTkwMiIsImVtYWlsIjoiYW5pa2NoYWtAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJhbmlrZXQgY2hha3JhYm9ydHkiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUNnOG9jSmE0SEdidVRNM3JnMmRnQzByOVBZbWF4aXR0ZmhyZUwyVk4yWXBhaFc3LVQ3Z2NnPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6ImFuaWtldCIsImZhbWlseV9uYW1lIjoiY2hha3JhYm9ydHkiLCJpYXQiOjE3Mzc4OTIwNDIsImV4cCI6MTczNzg5NTY0Mn0.Idf9VNDkgZ9MopobH9vL-NDY2yNcNCL5mzQzpCnFoV4L82aVVRXa8VCOLUlq5EyqrWTIX7e3BtEz7dURuQ8_q4oOSzSvOYxjEsDzLtHz-Y-a-4GmEjdq2dEeV04dWpKfrXtB9opAtNKn-TGl6i9TVRMNpymKG5ShK8he7xhiifSdhVKth09SjsE7KF48JIeZNs47AK4_OLy4r9heo7iel3CRKv9_sh76mVig5aNJ7L0v-c"; // Replace with the actual ID token

        try {
            boolean isValid = verifyIdToken(idToken);
            if (isValid) {
                System.out.println("ID Token is valid.");
            } else {
                System.out.println("ID Token is invalid.");
            }
        } catch (GeneralSecurityException | IOException e) {
            System.err.println("Error verifying ID token: " + e.getMessage());
        }
    }

    private static boolean verifyIdToken(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        com.google.api.client.googleapis.auth.oauth2.GoogleIdToken idTokenObject = verifier.verify(idToken);

        return idTokenObject != null;
    }
}