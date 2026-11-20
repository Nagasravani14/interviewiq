package com.interviewiq.service;

import com.interviewiq.model.AssessmentType;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.repository.AttemptRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Computes analytics and performance statistics strictly derived from actual saved attempt records.
 */
public class AnalyticsService {
    private final AttemptRepository attemptRepository;

    public AnalyticsService(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public int getTotalAttempts() {
        return attemptRepository.findAll().size();
    }

    public double getAverageAccuracy() {
        List<AttemptRecord> records = attemptRepository.findAll();
        if (records.isEmpty()) return 0.0;
        double sum = records.stream().mapToDouble(AttemptRecord::getAccuracyPercentage).sum();
        return Math.round((sum / records.size()) * 10.0) / 10.0;
    }

    public int getCodingProblemsSolved() {
        return (int) attemptRepository.findAll().stream()
                .filter(r -> r.getType() == AssessmentType.CODING_ASSESSMENT)
                .filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus()))
                .count();
    }

    public int getMockInterviewsCompleted() {
        return (int) attemptRepository.findAll().stream()
                .filter(r -> r.getType() == AssessmentType.MOCK_INTERVIEW)
                .count();
    }

    public int getTotalQuizzesCompleted() {
        return (int) attemptRepository.findAll().stream()
                .filter(r -> r.getType() == AssessmentType.QUIZ)
                .count();
    }

    public long getTotalStudyTimeSeconds() {
        return attemptRepository.findAll().stream()
                .mapToLong(AttemptRecord::getDurationSeconds)
                .sum();
    }

    public int calculateStudyStreakDays() {
        List<AttemptRecord> records = attemptRepository.findAll();
        if (records.isEmpty()) return 0;

        Set<LocalDate> dates = records.stream()
                .map(r -> r.getTimestamp().toLocalDate())
                .collect(Collectors.toSet());

        LocalDate current = LocalDate.now();
        // If no attempt today, check if yesterday had an attempt to keep streak alive
        if (!dates.contains(current)) {
            current = current.minusDays(1);
            if (!dates.contains(current)) {
                return 0;
            }
        }

        int streak = 0;
        while (dates.contains(current)) {
            streak++;
            current = current.minusDays(1);
        }
        return streak;
    }

    /**
     * Returns weekly attempt counts for the past 7 days (e.g. "Mon" -> 2, "Tue" -> 0, etc.)
     */
    public Map<String, Integer> getWeeklyActivity() {
        Map<String, Integer> result = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE");

        // Initialize 7 days ending today
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            result.put(date.format(dayFormatter), 0);
        }

        List<AttemptRecord> records = attemptRepository.findAll();
        for (AttemptRecord r : records) {
            LocalDate recordDate = r.getTimestamp().toLocalDate();
            for (int i = 6; i >= 0; i--) {
                LocalDate date = today.minusDays(i);
                if (recordDate.equals(date)) {
                    String label = date.format(dayFormatter);
                    result.put(label, result.getOrDefault(label, 0) + 1);
                }
            }
        }

        return result;
    }

    /**
     * Topic-wise average accuracy percentage.
     */
    public Map<String, Double> getCategoryAccuracyMap() {
        Map<String, List<Double>> grouped = new HashMap<>();
        for (AttemptRecord r : attemptRepository.findAll()) {
            if (r.getCategory() != null && !r.getCategory().isEmpty()) {
                grouped.computeIfAbsent(r.getCategory(), k -> new ArrayList<>()).add(r.getAccuracyPercentage());
            }
        }

        Map<String, Double> result = new LinkedHashMap<>();
        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    double avg = e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    result.put(e.getKey(), Math.round(avg * 10.0) / 10.0);
                });
        return result;
    }

    /**
     * Topic-wise attempt counts.
     */
    public Map<String, Integer> getCategoryAttemptCountMap() {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (AttemptRecord r : attemptRepository.findAll()) {
            if (r.getCategory() != null && !r.getCategory().isEmpty()) {
                result.put(r.getCategory(), result.getOrDefault(r.getCategory(), 0) + 1);
            }
        }
        return result;
    }

    /**
     * Distribution of attempts by AssessmentType.
     */
    public Map<AssessmentType, Integer> getAssessmentTypeDistribution() {
        Map<AssessmentType, Integer> map = new LinkedHashMap<>();
        map.put(AssessmentType.QUIZ, 0);
        map.put(AssessmentType.MOCK_INTERVIEW, 0);
        map.put(AssessmentType.CODING_ASSESSMENT, 0);
        map.put(AssessmentType.PRACTICE, 0);

        for (AttemptRecord r : attemptRepository.findAll()) {
            if (r.getType() != null) {
                map.put(r.getType(), map.getOrDefault(r.getType(), 0) + 1);
            }
        }
        return map;
    }

    /**
     * Returns top 3 strongest topics by accuracy.
     */
    public List<Map.Entry<String, Double>> getTopStrengths(int limit) {
        return getCategoryAccuracyMap().entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Returns top topics needing improvement (lowest accuracy).
     */
    public List<Map.Entry<String, Double>> getTopicsNeedingImprovement(int limit) {
        return getCategoryAccuracyMap().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Returns the most recent N attempts.
     */
    public List<AttemptRecord> getRecentAttempts(int limit) {
        return attemptRepository.findAll().stream()
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
