package com.interviewiq.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreCalculatorTest {

    @Test
    @DisplayName("Test accuracy calculation and rounding")
    void testCalculateAccuracy() {
        assertEquals(100.0, ScoreCalculator.calculateAccuracy(5, 5));
        assertEquals(80.0, ScoreCalculator.calculateAccuracy(4, 5));
        assertEquals(33.3, ScoreCalculator.calculateAccuracy(1, 3));
        assertEquals(0.0, ScoreCalculator.calculateAccuracy(0, 5));
        assertEquals(0.0, ScoreCalculator.calculateAccuracy(0, 0));
    }

    @Test
    @DisplayName("Test letter grade assignments")
    void testGradeAssignment() {
        assertTrue(ScoreCalculator.getGrade(95.0).startsWith("A+"));
        assertTrue(ScoreCalculator.getGrade(82.0).startsWith("A"));
        assertTrue(ScoreCalculator.getGrade(75.0).startsWith("B"));
        assertTrue(ScoreCalculator.getGrade(65.0).startsWith("C"));
        assertTrue(ScoreCalculator.getGrade(55.0).startsWith("D"));
        assertTrue(ScoreCalculator.getGrade(40.0).startsWith("F"));
    }

    @Test
    @DisplayName("Test interview readiness level classification")
    void testReadinessLevel() {
        assertEquals("Interview Ready", ScoreCalculator.getReadinessLevel(90.0));
        assertEquals("Strong Foundation", ScoreCalculator.getReadinessLevel(75.0));
        assertEquals("In Progress", ScoreCalculator.getReadinessLevel(55.0));
        assertEquals("Early Stage", ScoreCalculator.getReadinessLevel(30.0));
    }
}
