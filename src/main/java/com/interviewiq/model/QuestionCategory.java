package com.interviewiq.model;

/**
 * Standard interview and assessment categories.
 */
public enum QuestionCategory {
    ALL("All Categories"),
    JAVA("Java Programming"),
    PYTHON("Python Programming"),
    DSA("Data Structures & Algorithms"),
    OOP("Object-Oriented Programming"),
    DBMS("Database Management Systems"),
    OS("Operating Systems"),
    NETWORKS("Computer Networks"),
    SOFTWARE_ENGINEERING("Software Engineering");

    private final String displayName;

    QuestionCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static QuestionCategory fromString(String val) {
        if (val == null || val.trim().isEmpty()) return ALL;
        for (QuestionCategory c : values()) {
            if (c.name().equalsIgnoreCase(val) || c.displayName.equalsIgnoreCase(val)) {
                return c;
            }
        }
        return ALL;
    }
}
