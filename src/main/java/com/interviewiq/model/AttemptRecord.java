package com.interviewiq.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Persistent record of any assessment attempt (Quiz, Mock Interview, Coding Assessment).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttemptRecord {
    private String id;
    private AssessmentType type;
    private String title;
    private String category;
    private Difficulty difficulty;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private long durationSeconds;
    private int totalItems;
    private int completedItems;
    private double score;
    private double maxScore;
    private double accuracyPercentage;
    private String status;
    private String notes;
    private Map<String, String> details = new HashMap<>();

    public AttemptRecord() {}

    public AttemptRecord(String id, AssessmentType type, String title, String category,
                         Difficulty difficulty, LocalDateTime timestamp, long durationSeconds,
                         int totalItems, int completedItems, double score, double maxScore,
                         double accuracyPercentage, String status, String notes) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.durationSeconds = durationSeconds;
        this.totalItems = totalItems;
        this.completedItems = completedItems;
        this.score = score;
        this.maxScore = maxScore;
        this.accuracyPercentage = accuracyPercentage;
        this.status = status;
        this.notes = notes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public AssessmentType getType() { return type; }
    public void setType(AssessmentType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(long durationSeconds) { this.durationSeconds = durationSeconds; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public int getCompletedItems() { return completedItems; }
    public void setCompletedItems(int completedItems) { this.completedItems = completedItems; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public double getMaxScore() { return maxScore; }
    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }

    public double getAccuracyPercentage() { return accuracyPercentage; }
    public void setAccuracyPercentage(double accuracyPercentage) { this.accuracyPercentage = accuracyPercentage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Map<String, String> getDetails() { return details; }
    public void setDetails(Map<String, String> details) { this.details = details != null ? details : new HashMap<>(); }
}
