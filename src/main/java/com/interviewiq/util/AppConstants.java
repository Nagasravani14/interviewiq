package com.interviewiq.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Application-wide constants for InterviewIQ.
 */
public final class AppConstants {
    private AppConstants() {}

    public static final String APP_NAME = "InterviewIQ";
    public static final String APP_SUBTITLE = "Smart Mock Interview & Assessment Platform";
    public static final String APP_VERSION = "1.0.0";

    // Application Storage
    public static final String APP_DATA_DIR_NAME = ".interviewiq";
    public static final String PREFERENCES_FILE = "user_preferences.json";
    public static final String HISTORY_FILE = "attempt_history.json";
    public static final String QUESTION_STATE_FILE = "question_state.json";

    // Standard Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_REGULAR_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_CODE = new Font("Consolas", Font.PLAIN, 13);
    public static final Font FONT_BIG_STAT = new Font("Segoe UI", Font.BOLD, 26);

    /**
     * Resolves the application data storage directory safely with local fallback.
     */
    public static java.nio.file.Path resolveDataDirectory() {
        java.nio.file.Path userHomeDir = java.nio.file.Paths.get(System.getProperty("user.home"), APP_DATA_DIR_NAME);
        try {
            if (!java.nio.file.Files.exists(userHomeDir)) {
                java.nio.file.Files.createDirectories(userHomeDir);
            }
            java.nio.file.Path test = userHomeDir.resolve(".write_test");
            java.nio.file.Files.writeString(test, "ok");
            java.nio.file.Files.deleteIfExists(test);
            return userHomeDir;
        } catch (Exception e) {
            java.nio.file.Path localDir = java.nio.file.Paths.get(".", APP_DATA_DIR_NAME);
            try {
                if (!java.nio.file.Files.exists(localDir)) {
                    java.nio.file.Files.createDirectories(localDir);
                }
            } catch (Exception ignored) {}
            return localDir;
        }
    }
}
