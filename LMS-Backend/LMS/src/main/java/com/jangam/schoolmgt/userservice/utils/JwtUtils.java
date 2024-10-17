package com.jangam.schoolmgt.userservice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtUtils {

    private final String Secret_Key = "12321123134aergaetgaeg46346srths56uy56735yrgs5yw54w56w4tgsergw54645624tet5tgs45";
    private final Long expiration = 86400000L;

    public String generateToken(String username){
        Date dateTime = new Date();
        Date expiryDate = new Date(dateTime.getTime() + expiration);

        Key signingKey = new SecretKeySpec(Secret_Key.getBytes(),SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(dateTime)
                .setExpiration(expiryDate)
                .signWith(signingKey)
                .compact();
    }

}
