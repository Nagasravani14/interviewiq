package com.interviewiq.model;

/**
 * Summarizes preparation progress for a specific category/topic.
 */
public class TopicProgress {
    private final String category;
    private final int totalQuestions;
    private final int understoodQuestions;
    private final int bookmarkedQuestions;
    private final double accuracyPercentage;

    public TopicProgress(String category, int totalQuestions, int understoodQuestions,
                         int bookmarkedQuestions, double accuracyPercentage) {
        this.category = category;
        this.totalQuestions = totalQuestions;
        this.understoodQuestions = understoodQuestions;
        this.bookmarkedQuestions = bookmarkedQuestions;
        this.accuracyPercentage = accuracyPercentage;
    }

    public String getCategory() { return category; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getUnderstoodQuestions() { return understoodQuestions; }
    public int getBookmarkedQuestions() { return bookmarkedQuestions; }
    public double getAccuracyPercentage() { return accuracyPercentage; }

    public double getCompletionRate() {
        if (totalQuestions <= 0) return 0.0;
        return ((double) understoodQuestions / totalQuestions) * 100.0;
    }
}
