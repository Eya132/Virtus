package edu.pidev3a8.utils;

import java.util.UUID;

public class generateResetToken {

    public static String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}