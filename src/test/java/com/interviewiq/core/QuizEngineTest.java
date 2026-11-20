package com.interviewiq.core;

import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.QuizQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizEngineTest {
    private List<QuizQuestion> sampleQuestions;

    @BeforeEach
    void setUp() {
        sampleQuestions = new ArrayList<>();
        sampleQuestions.add(new QuizQuestion("Q1", "Java Programming", Difficulty.EASY,
                "What is JVM?", Arrays.asList("Virtual Machine", "Compiler", "Debugger", "Editor"), 0, "JVM is Java Virtual Machine"));
        sampleQuestions.add(new QuizQuestion("Q2", "Java Programming", Difficulty.MEDIUM,
                "Default value of boolean?", Arrays.asList("true", "false", "null", "0"), 1, "Default is false"));
        sampleQuestions.add(new QuizQuestion("Q3", "Java Programming", Difficulty.HARD,
                "Size of int in Java?", Arrays.asList("16-bit", "32-bit", "64-bit", "8-bit"), 1, "int is 32-bit"));
    }

    @Test
    @DisplayName("Test correct answer scoring in QuizEngine")
    void testScoringAllCorrect() {
        QuizEngine engine = new QuizEngine("Java Programming", Difficulty.ALL, sampleQuestions, 3, 300);
        assertEquals(3, engine.getTotalQuestions());

        // Answer all questions correctly according to their indices in the engine
        for (int i = 0; i < engine.getTotalQuestions(); i++) {
            engine.jumpTo(i);
            engine.selectAnswer(engine.getCurrentQuestion().getCorrectOptionIndex());
        }

        assertEquals(3, engine.calculateCorrectCount());
        assertEquals(100.0, engine.calculateAccuracy());

        AttemptRecord record = engine.submit();
        assertEquals(100.0, record.getAccuracyPercentage());
        assertEquals(3.0, record.getScore());
        assertEquals("COMPLETED", record.getStatus());
    }

    @Test
    @DisplayName("Test incorrect answer scoring in QuizEngine")
    void testScoringIncorrect() {
        QuizEngine engine = new QuizEngine("Java Programming", Difficulty.ALL, sampleQuestions, 3, 300);

        for (int i = 0; i < engine.getTotalQuestions(); i++) {
            engine.jumpTo(i);
            int wrong = (engine.getCurrentQuestion().getCorrectOptionIndex() + 1) % 4;
            engine.selectAnswer(wrong);
        }

        assertEquals(0, engine.calculateCorrectCount());
        assertEquals(0.0, engine.calculateAccuracy());

        AttemptRecord record = engine.submit();
        assertEquals(0.0, record.getScore());
    }

    @Test
    @DisplayName("Test empty submission")
    void testEmptySubmission() {
        QuizEngine engine = new QuizEngine("Java Programming", Difficulty.ALL, sampleQuestions, 3, 300);

        assertEquals(0, engine.getAnsweredCount());
        assertEquals(3, engine.getUnansweredCount());

        AttemptRecord record = engine.submit();
        assertEquals(0, record.getCompletedItems());
        assertEquals(0.0, record.getScore());
    }

    @Test
    @DisplayName("Test partial submission")
    void testPartialSubmission() {
        QuizEngine engine = new QuizEngine("Java Programming", Difficulty.ALL, sampleQuestions, 3, 300);
        engine.jumpTo(0);
        engine.selectAnswer(engine.getCurrentQuestion().getCorrectOptionIndex());

        assertEquals(1, engine.getAnsweredCount());
        assertEquals(2, engine.getUnansweredCount());

        AttemptRecord record = engine.submit();
        assertEquals(1, record.getCompletedItems());
        assertEquals(1.0, record.getScore());
        assertEquals(33.3, record.getAccuracyPercentage());
    }

    @Test
    @DisplayName("Test timer decrement and flag toggle")
    void testTimerAndFlagging() {
        QuizEngine engine = new QuizEngine("Java Programming", Difficulty.ALL, sampleQuestions, 3, 100);
        assertEquals(100, engine.getRemainingSeconds());

        engine.decrementTimer();
        assertEquals(99, engine.getRemainingSeconds());

        assertFalse(engine.isFlagged(0));
        engine.toggleFlag(0);
        assertTrue(engine.isFlagged(0));
        engine.toggleFlag(0);
        assertFalse(engine.isFlagged(0));
    }
}
