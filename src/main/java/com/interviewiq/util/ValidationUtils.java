package com.interviewiq.util;

/**
 * Validation utilities for user input and assessment configuration.
 */
public final class ValidationUtils {
    private ValidationUtils() {}

    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static String sanitize(String input, String defaultValue) {
        if (input == null || input.trim().isEmpty()) {
            return defaultValue;
        }
        return input.trim();
    }

    public static int clamp(int val, int min, int max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }
}
