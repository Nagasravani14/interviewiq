package com.interviewiq.service;

import com.interviewiq.model.AssessmentType;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.Difficulty;
import com.interviewiq.repository.AttemptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyticsServiceTest {

    private static class InMemoryAttemptRepository implements AttemptRepository {
        private final List<AttemptRecord> records = new ArrayList<>();

        @Override public List<AttemptRecord> findAll() { return new ArrayList<>(records); }
        @Override public void save(AttemptRecord record) { records.add(record); }
        @Override public void saveAll(List<AttemptRecord> list) { records.addAll(list); }
        @Override public void deleteById(String id) { records.removeIf(r -> r.getId().equals(id)); }
        @Override public void clearAll() { records.clear(); }
    }

    private InMemoryAttemptRepository repository;
    private AnalyticsService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAttemptRepository();
        service = new AnalyticsService(repository);
    }

    @Test
    @DisplayName("Test metrics on empty repository")
    void testEmptyHistoryMetrics() {
        assertEquals(0, service.getTotalAttempts());
        assertEquals(0.0, service.getAverageAccuracy());
        assertEquals(0, service.getCodingProblemsSolved());
        assertEquals(0, service.getMockInterviewsCompleted());
        assertEquals(0, service.calculateStudyStreakDays());
        assertTrue(service.getCategoryAccuracyMap().isEmpty());
    }

    @Test
    @DisplayName("Test analytics calculation with recorded attempts")
    void testCalculationsWithData() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Quiz: 80% accuracy
        repository.save(new AttemptRecord("1", AssessmentType.QUIZ, "Java Quiz", "Java Programming",
                Difficulty.MEDIUM, now, 120, 5, 5, 4.0, 5.0, 80.0, "COMPLETED", "A"));

        // 2. Coding: 100% accuracy, completed
        repository.save(new AttemptRecord("2", AssessmentType.CODING_ASSESSMENT, "Two Sum", "Data Structures & Algorithms",
                Difficulty.EASY, now, 200, 4, 4, 4.0, 4.0, 100.0, "COMPLETED", "All passed"));

        // 3. Mock Interview: 60% score
        repository.save(new AttemptRecord("3", AssessmentType.MOCK_INTERVIEW, "Mock OOP", "Object-Oriented Programming",
                Difficulty.HARD, now, 350, 3, 3, 60.0, 100.0, 60.0, "COMPLETED", "C"));

        assertEquals(3, service.getTotalAttempts());
        assertEquals(80.0, service.getAverageAccuracy()); // (80 + 100 + 60) / 3 = 80.0
        assertEquals(1, service.getCodingProblemsSolved());
        assertEquals(1, service.getMockInterviewsCompleted());
        assertEquals(1, service.getTotalQuizzesCompleted());
        assertEquals(670, service.getTotalStudyTimeSeconds());

        // Weekly activity should have total of 3 attempts
        Map<String, Integer> weekly = service.getWeeklyActivity();
        assertNotNull(weekly);
        int totalWeekly = weekly.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(3, totalWeekly);

        // Check topic accuracy map
        Map<String, Double> topicAcc = service.getCategoryAccuracyMap();
        assertEquals(80.0, topicAcc.get("Java Programming"));
        assertEquals(100.0, topicAcc.get("Data Structures & Algorithms"));
        assertEquals(60.0, topicAcc.get("Object-Oriented Programming"));

        // Check streak
        assertEquals(1, service.calculateStudyStreakDays());
    }
}
