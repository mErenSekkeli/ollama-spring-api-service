package org.erensekkeli.chatbotservice.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class RandomKeyGenerator {

    public static String generateComplexId() {
        String uuidPart = UUID.randomUUID().toString();
        String timestampPart = Long.toString(System.currentTimeMillis());
        String currentString = uuidPart + timestampPart;

        char[] currentArray = currentString.toCharArray();

        for (int i = 0; i < currentArray.length; i++) {
            int randomIndex = (int) (Math.random() * currentArray.length);
            char temp = currentArray[i];
            currentArray[i] = currentArray[randomIndex];
            currentArray[randomIndex] = temp;
        }
        return new String(currentArray);
    }

    public static String generateRandomToken() {
        String random = UUID.randomUUID().toString().replaceAll("-", "");
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[128];
        secureRandom.nextBytes(key);
        return random + Base64.getUrlEncoder().withoutPadding().encodeToString(key).replaceAll("-", "");
    }
}
