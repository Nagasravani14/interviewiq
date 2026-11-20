package com.interviewiq.model;

/**
 * Result of evaluating a test case.
 */
public class TestResult {
    private final int testCaseIndex;
    private final String input;
    private final String expectedOutput;
    private final String actualOutput;
    private final boolean passed;
    private final long executionTimeMs;
    private final String message;

    public TestResult(int testCaseIndex, String input, String expectedOutput,
                      String actualOutput, boolean passed, long executionTimeMs, String message) {
        this.testCaseIndex = testCaseIndex;
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.actualOutput = actualOutput;
        this.passed = passed;
        this.executionTimeMs = executionTimeMs;
        this.message = message;
    }

    public int getTestCaseIndex() { return testCaseIndex; }
    public String getInput() { return input; }
    public String getExpectedOutput() { return expectedOutput; }
    public String getActualOutput() { return actualOutput; }
    public boolean isPassed() { return passed; }
    public long getExecutionTimeMs() { return executionTimeMs; }
    public String getMessage() { return message; }
}
