package com.interviewiq.core;

import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.CodingChallenge;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AssessmentEngineTest {
    private AssessmentEngine engine;
    private CodingChallenge reverseArrayChallenge;

    @BeforeEach
    void setUp() {
        engine = new AssessmentEngine();
        reverseArrayChallenge = new CodingChallenge(
                "COD-001",
                "Reverse an Array",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                "Reverse array in place",
                "int[] nums",
                "int[] reversed",
                "1 <= n <= 100",
                "public int[] reverseArray(int[] nums) { return nums; }",
                "Two pointers",
                Arrays.asList(
                        new TestCase("1, 2, 3", "[3, 2, 1]", "Basic", false),
                        new TestCase("5", "[5]", "Single", false)
                ),
                Arrays.asList("Array", "Two Pointers")
        );
    }

    @Test
    @DisplayName("Test evaluation of valid algorithm implementation")
    void testValidSubmission() {
        String code = "public class Solution {\n" +
                "    public int[] reverseArray(int[] nums) {\n" +
                "        int left = 0, right = nums.length - 1;\n" +
                "        while (left < right) {\n" +
                "            int t = nums[left]; nums[left] = nums[right]; nums[right] = t;\n" +
                "            left++; right--;\n" +
                "        }\n" +
                "        return nums;\n" +
                "    }\n" +
                "}";

        AssessmentEngine.EvaluationSummary summary = engine.evaluateChallenge(reverseArrayChallenge, code);
        assertTrue(summary.isAllPassed());
        assertEquals(2, summary.getPassedCount());
        assertEquals(100.0, summary.getScorePercentage());

        AttemptRecord record = engine.createAttemptRecord(summary, 45);
        assertEquals("COMPLETED", record.getStatus());
        assertEquals(100.0, record.getAccuracyPercentage());
    }

    @Test
    @DisplayName("Test evaluation of empty submission")
    void testEmptySubmission() {
        AssessmentEngine.EvaluationSummary summary = engine.evaluateChallenge(reverseArrayChallenge, "");
        assertFalse(summary.isAllPassed());
        assertEquals(0, summary.getPassedCount());
        assertEquals(0.0, summary.getScorePercentage());
    }

    @Test
    @DisplayName("Test evaluation with syntax error (missing brackets/return)")
    void testInvalidSyntaxSubmission() {
        String brokenCode = "public int[] reverseArray(int[] nums) left = 0;";
        AssessmentEngine.EvaluationSummary summary = engine.evaluateChallenge(reverseArrayChallenge, brokenCode);
        assertFalse(summary.isAllPassed());
        assertEquals(0, summary.getPassedCount());
    }
}
