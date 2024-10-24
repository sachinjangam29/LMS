package com.jangam.schoolmgt.userservice.utils;

import com.jangam.schoolmgt.userservice.config.UserDetailsImpl;
import com.jangam.schoolmgt.userservice.config.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Ensure the key is at least 256 bits (32 characters long) for HS256
//    @Value("${bezkoder.app.jwtsecret}")
    private final String SECRET_KEY = "=====================================BezKoder=Spring======================================";// Replace this with your secure key for production
    private final Long expiration = 120000L; // Token expiration time in milliseconds (30 seconds)

    //Refresh Token
    private final Long jwtRefreshTokenExpiration = 300000L;

    // Generates a JWT token for a given username
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Date currentTime = new Date();
        Date expiryDate = new Date(currentTime.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Setting subject as username
                .setIssuedAt(currentTime) // Current time for issuedAt
                .setExpiration(expiryDate) // Expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Use the secure key for signing
                .compact();
    }

    public String generateRefreshToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtRefreshTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Method to decode the secret and create the signing key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Use Base64 to decode the key if it's encoded
        return Keys.hmacShaKeyFor(keyBytes); // Generate a SecretKey using the byte array
    }

    // Retrieves the username (subject) from the token
    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Setting the signing key
                .build()
                .parseClaimsJws(token) // Parse the token
                .getBody() // Get the body (claims)
                .getSubject(); // Return the username
    }

    // Validates the token for its integrity and expiration
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Set the signing key
                    .build()
                    .parseClaimsJws(token); // Parse and validate the token
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unknown exception: {} ", e.getMessage());
        }
        return false;
    }


    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Set the signing key
                    .build()
                    .parseClaimsJws(token); // Parse and validate the token
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unknown exception: {} ", e.getMessage());
        }
        return false;

    }
}
