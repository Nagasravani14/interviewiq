package com.interviewiq.core;

import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InterviewEngineTest {
    private List<Question> sampleQuestions;

    @BeforeEach
    void setUp() {
        sampleQuestions = new ArrayList<>();
        sampleQuestions.add(new Question(
                "JAVA-001",
                "JVM Architecture",
                "Java Programming",
                Difficulty.MEDIUM,
                Arrays.asList("JVM", "Memory", "Heap"),
                "Explain the JVM memory areas.",
                "Heap, Stack, Metaspace, PC Register",
                "Detailed explanation of heap and stack",
                "Person p = new Person();"
        ));
        sampleQuestions.add(new Question(
                "JAVA-002",
                "String Immutability",
                "Java Programming",
                Difficulty.EASY,
                Arrays.asList("String", "Immutability"),
                "Why is String immutable in Java?",
                "Security, caching in String Pool, thread-safety",
                "Strings cannot be altered once created",
                "String s = \"hello\";"
        ));
    }

    @Test
    @DisplayName("Test InterviewEngine session initialization and navigation")
    void testSessionInitAndNavigation() {
        InterviewEngine engine = new InterviewEngine("Java Programming", Difficulty.ALL, sampleQuestions, 2, 600);
        assertEquals(2, engine.getTotalQuestions());
        assertEquals(0, engine.getCurrentIndex());

        assertTrue(engine.hasNext());
        engine.next();
        assertEquals(1, engine.getCurrentIndex());
        assertFalse(engine.hasNext());

        assertTrue(engine.hasPrevious());
        engine.previous();
        assertEquals(0, engine.getCurrentIndex());
    }

    @Test
    @DisplayName("Test rule-based answer scoring and keywords detection")
    void testRuleBasedEvaluation() {
        InterviewEngine engine = new InterviewEngine("Java Programming", Difficulty.ALL, sampleQuestions, 2, 600);

        // Find the question about JVM
        int jvmIndex = -1;
        for (int i = 0; i < engine.getTotalQuestions(); i++) {
            if (engine.getQuestions().get(i).getId().equals("JAVA-001")) {
                jvmIndex = i;
                break;
            }
        }
        assertTrue(jvmIndex >= 0);

        engine.jumpTo(jvmIndex);

        // Submitting a comprehensive answer matching tags and structure
        String richAnswer = "The JVM architecture consists of several runtime data areas including the Heap memory " +
                "for objects, Thread Stack for local frames, and Metaspace for class metadata.\n" +
                "For example, when we create Person p = new Person(), the reference lives on stack while object is on heap.\n" +
                "1. Heap is shared.\n2. Stack is thread-private.";

        engine.saveAnswer(jvmIndex, richAnswer);
        assertTrue(engine.isAnswered(jvmIndex));

        double score = engine.evaluateAnswer(jvmIndex);
        assertTrue(score >= 70.0, "Comprehensive answer should score >= 70, got: " + score);

        // Submitting an empty answer on the other question
        int otherIndex = (jvmIndex == 0) ? 1 : 0;
        engine.jumpTo(otherIndex);
        assertEquals(0.0, engine.evaluateAnswer(otherIndex));
    }

    @Test
    @DisplayName("Test interview submission creates complete AttemptRecord")
    void testSubmissionRecord() {
        InterviewEngine engine = new InterviewEngine("Java Programming", Difficulty.ALL, sampleQuestions, 2, 300);
        engine.jumpTo(0);
        engine.saveAnswer(0, "JVM divides memory into heap and stack. Heap holds objects and stack holds execution frames.");

        AttemptRecord record = engine.submit();
        assertNotNull(record);
        assertEquals(2, record.getTotalItems());
        assertEquals(1, record.getCompletedItems());
        assertEquals("PARTIAL", record.getStatus());
        assertTrue(record.getDurationSeconds() > 0);
    }
}
