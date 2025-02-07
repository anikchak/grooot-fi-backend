package com.grooot.utility;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.google.api.client.util.Value;
import java.util.Date;
import java.util.Map;
import java.util.Base64;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${accessToken.expiration.millis}")
    private long ACCESS_TOKEN_EXPIRY;  
    @Value("${refreshToken.expiration.millis}")
    private long REFRESH_TOKEN_EXPIRY; 
    @Value("${sessionId.expiration.millis}")
    private long SESSION_ID_EXPIRY; 

    // Generate a strong 256-bit Secret Key
    @Value("${jwt.secret}")
    private String SECRET_STRING; // Replace with a securely generated key
    private SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_STRING));


    public String generateAccessToken(String userUUID) {
        return Jwts.builder()
        .claims(Map.of("userUUID", userUUID))  
        .subject("accessToken")
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + SESSION_ID_EXPIRY))
        .signWith(SECRET_KEY)
        .compact();
    }

    public String generateRefreshToken(String userUUID) {
        return Jwts.builder()
        .claims(Map.of("userUUID", userUUID))  
        .subject("refreshToken")
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + SESSION_ID_EXPIRY))
        .signWith(SECRET_KEY)
        .compact();
    }
    public String generateSessionId(String userUUID) {
        return Jwts.builder()
        .claims(Map.of("userUUID", userUUID))  
        .subject("sessionId")
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + SESSION_ID_EXPIRY))
        .signWith(SECRET_KEY)
        .compact();
    }

    public Claims extractClaims(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(SECRET_KEY) 
                .build()
                .parseSignedClaims(token); 

        return claimsJws.getPayload();
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
    // Extract userUUID from Token
    public String getUserUUID(String token) {
        return extractClaims(token).get("userUUID", String.class);
    }

    // Extract Subject (accessToken, refreshToken, sessionId)
    public String getSubject(String token) {
        return extractClaims(token).getSubject();
    }
}
