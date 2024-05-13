package org.erensekkeli.chatbotservice.util;

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
}
