package com.interviewiq.model;

/**
 * Standard difficulty levels.
 */
public enum Difficulty {
    ALL("All Levels"),
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard");

    private final String displayName;

    Difficulty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Difficulty fromString(String val) {
        if (val == null) return MEDIUM;
        for (Difficulty d : values()) {
            if (d.name().equalsIgnoreCase(val) || d.displayName.equalsIgnoreCase(val)) {
                return d;
            }
        }
        return MEDIUM;
    }
}
