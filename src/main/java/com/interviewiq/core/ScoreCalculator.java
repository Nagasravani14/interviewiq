package com.interviewiq.core;

/**
 * Utility for computing scores, accuracy percentages, letter grades, and performance remarks.
 */
public final class ScoreCalculator {
    private ScoreCalculator() {}

    public static double calculateAccuracy(int correct, int total) {
        if (total <= 0) return 0.0;
        double accuracy = ((double) correct / total) * 100.0;
        return Math.round(accuracy * 10.0) / 10.0;
    }

    public static String getGrade(double accuracy) {
        if (accuracy >= 90.0) return "A+ (Outstanding)";
        if (accuracy >= 80.0) return "A (Excellent)";
        if (accuracy >= 70.0) return "B (Proficient)";
        if (accuracy >= 60.0) return "C (Adequate)";
        if (accuracy >= 50.0) return "D (Needs Revision)";
        return "F (Underprepared)";
    }

    public static String getReadinessLevel(double overallScore) {
        if (overallScore >= 85.0) return "Interview Ready";
        if (overallScore >= 70.0) return "Strong Foundation";
        if (overallScore >= 50.0) return "In Progress";
        return "Early Stage";
    }

    public static String getFeedbackSummary(double accuracy, int totalQuestions, long durationSeconds) {
        StringBuilder sb = new StringBuilder();
        if (accuracy >= 85.0) {
            sb.append("Great job! You demonstrated strong mastery of the concepts covered in this session.");
        } else if (accuracy >= 70.0) {
            sb.append("Solid performance! You have a good grasp of the fundamentals, with a few areas to refine.");
        } else if (accuracy >= 50.0) {
            sb.append("Fair attempt. We recommend reviewing the detailed explanations and practicing weaker topics.");
        } else {
            sb.append("Needs focus. Spend more time in Interview Prep and Practice Arena before retaking.");
        }
        return sb.toString();
    }
}
