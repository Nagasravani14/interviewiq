package com.interviewiq.service;

import com.interviewiq.model.AssessmentType;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.Difficulty;
import com.interviewiq.repository.AttemptRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service managing persistent assessment history records.
 */
public class HistoryService {
    private final AttemptRepository repository;

    public HistoryService(AttemptRepository repository) {
        this.repository = repository;
    }

    public List<AttemptRecord> getAllAttempts() {
        return repository.findAll();
    }

    public void recordAttempt(AttemptRecord record) {
        if (record != null) {
            repository.save(record);
        }
    }

    public void deleteAttempt(String id) {
        repository.deleteById(id);
    }

    public void clearHistory() {
        repository.clearAll();
    }

    public List<AttemptRecord> filterAttempts(AssessmentType type, String category, String searchQuery, String sortOrder) {
        List<AttemptRecord> records = repository.findAll();

        return records.stream()
                .filter(r -> {
                    if (type != null && type != AssessmentType.ALL) {
                        if (r.getType() != type) return false;
                    }
                    if (category != null && !category.equalsIgnoreCase("All Categories") && !category.equalsIgnoreCase("All")) {
                        if (!r.getCategory().equalsIgnoreCase(category)) return false;
                    }
                    if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                        String q = searchQuery.trim().toLowerCase(Locale.ROOT);
                        boolean matchesTitle = r.getTitle().toLowerCase(Locale.ROOT).contains(q);
                        boolean matchesCat = r.getCategory().toLowerCase(Locale.ROOT).contains(q);
                        boolean matchesNotes = r.getNotes() != null && r.getNotes().toLowerCase(Locale.ROOT).contains(q);
                        if (!matchesTitle && !matchesCat && !matchesNotes) return false;
                    }
                    return true;
                })
                .sorted((a, b) -> {
                    if ("OLD_FIRST".equalsIgnoreCase(sortOrder)) {
                        return a.getTimestamp().compareTo(b.getTimestamp());
                    } else if ("SCORE_HIGH".equalsIgnoreCase(sortOrder)) {
                        return Double.compare(b.getAccuracyPercentage(), a.getAccuracyPercentage());
                    } else if ("DURATION_LONG".equalsIgnoreCase(sortOrder)) {
                        return Long.compare(b.getDurationSeconds(), a.getDurationSeconds());
                    } else {
                        // Default: NEW_FIRST
                        return b.getTimestamp().compareTo(a.getTimestamp());
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Injects realistic starter demo dataset (clearly marked with 'Demo') for first-time visual exploration.
     */
    public void loadDemoData() {
        LocalDateTime now = LocalDateTime.now();
        List<AttemptRecord> demoList = new ArrayList<>();

        // Demo 1: Java Quiz
        AttemptRecord r1 = new AttemptRecord(
                UUID.randomUUID().toString(),
                AssessmentType.QUIZ,
                "Quiz: Java Programming (Medium) [Demo]",
                "Java Programming",
                Difficulty.MEDIUM,
                now.minusDays(3).minusHours(2),
                280, // 4m 40s
                5,
                5,
                4.0,
                5.0,
                80.0,
                "COMPLETED",
                "A (Excellent)"
        );
        demoList.add(r1);

        // Demo 2: Coding Assessment (Kadane's)
        AttemptRecord r2 = new AttemptRecord(
                UUID.randomUUID().toString(),
                AssessmentType.CODING_ASSESSMENT,
                "Coding: Maximum Subarray Sum [Demo]",
                "Data Structures & Algorithms",
                Difficulty.MEDIUM,
                now.minusDays(2).minusHours(4),
                410, // 6m 50s
                4,
                4,
                4.0,
                4.0,
                100.0,
                "COMPLETED",
                "All test cases passed! Optimal O(N) runtime."
        );
        demoList.add(r2);

        // Demo 3: Mock Interview (Full Stack / OOP)
        AttemptRecord r3 = new AttemptRecord(
                UUID.randomUUID().toString(),
                AssessmentType.MOCK_INTERVIEW,
                "Mock Interview: OOP Concepts [Demo]",
                "Object-Oriented Programming",
                Difficulty.MEDIUM,
                now.minusDays(1).minusHours(1),
                540, // 9 min
                3,
                3,
                88.0,
                100.0,
                88.0,
                "COMPLETED",
                "Interview Ready"
        );
        demoList.add(r3);

        // Demo 4: DBMS Quiz
        AttemptRecord r4 = new AttemptRecord(
                UUID.randomUUID().toString(),
                AssessmentType.QUIZ,
                "Quiz: Database Management Systems [Demo]",
                "Database Management Systems",
                Difficulty.HARD,
                now.minusHours(5),
                310,
                5,
                5,
                3.0,
                5.0,
                60.0,
                "COMPLETED",
                "C (Adequate)"
        );
        demoList.add(r4);

        repository.saveAll(demoList);
    }
}
