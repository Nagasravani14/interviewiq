package com.interviewiq.model;

/**
 * Types of assessments supported in InterviewIQ.
 */
public enum AssessmentType {
    ALL("All Types"),
    QUIZ("MCQ Quiz"),
    MOCK_INTERVIEW("Mock Interview"),
    CODING_ASSESSMENT("Coding Assessment"),
    PRACTICE("Practice Drill");

    private final String displayName;

    AssessmentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AssessmentType fromString(String val) {
        if (val == null) return QUIZ;
        for (AssessmentType t : values()) {
            if (t.name().equalsIgnoreCase(val) || t.displayName.equalsIgnoreCase(val)) {
                return t;
            }
        }
        return QUIZ;
    }
}
