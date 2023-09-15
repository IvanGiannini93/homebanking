package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {
    public static String getCardNumber() {
        StringBuilder number = new StringBuilder();
        int min = 1000;
        int max = 9999;
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            number.append(random.nextInt(max - min + 1) + min).append("-");
        }
        number = new StringBuilder(number.substring(0, number.length() - 1));
        return number.toString();
    }
    public static int getCvv() {
        int min = 100;
        int max = 999;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
