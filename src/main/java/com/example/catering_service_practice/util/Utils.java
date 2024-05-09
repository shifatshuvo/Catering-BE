package com.example.catering_service_practice.util;

public class Utils {

    public static String randomString(int n) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // generate a random number between 0 to characters variable length
            int index = (int)(characters.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
