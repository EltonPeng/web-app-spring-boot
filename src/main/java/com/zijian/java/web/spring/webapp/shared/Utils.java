package com.zijian.java.web.spring.webapp.shared;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

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

    private String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);

        for (int i =0; i<length; i++){
            result.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(result);
    }
}
