package com.zijian.java.web.spring.webapp.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import com.zijian.java.web.spring.webapp.security.SecurityConstants;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String generateUserId() {
        return generateRandomString(20);
    }

    public String generateAddressId() {
        return generateRandomString(20);
    }

    public String generateEmailVerificationToken(String publicUserId) {
        String token = Jwts.builder()
            .setSubject(publicUserId)
            .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
            .compact();
        
        return token;
    }

	public static boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token).getBody();

		return claims.getExpiration().before(new Date());
	}

    private String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);

        for (int i =0; i<length; i++){
            result.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(result);
    }
}
