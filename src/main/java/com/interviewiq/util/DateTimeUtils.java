package com.interviewiq.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility functions for formatting dates, durations, and timestamps.
 */
public final class DateTimeUtils {
    private DateTimeUtils() {}

    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    private static final DateTimeFormatter SHORT_DATE = DateTimeFormatter.ofPattern("MMM dd");

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(DISPLAY_FORMATTER);
    }

    public static String formatShortDate(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(SHORT_DATE);
    }

    public static String formatSecondsToMinutes(long totalSeconds) {
        if (totalSeconds < 0) totalSeconds = 0;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String formatDurationFriendly(long totalSeconds) {
        if (totalSeconds < 60) {
            return totalSeconds + "s";
        }
        long mins = totalSeconds / 60;
        long secs = totalSeconds % 60;
        if (secs == 0) {
            return mins + " min";
        }
        return mins + "m " + secs + "s";
    }
}
