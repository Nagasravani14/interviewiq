package com.interviewiq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Test case for coding assessment challenges.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {
    private String input;
    private String expectedOutput;
    private String explanation;
    private boolean hidden;

    public TestCase() {}

    public TestCase(String input, String expectedOutput, String explanation, boolean hidden) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.explanation = explanation;
        this.hidden = hidden;
    }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public String getExpectedOutput() { return expectedOutput; }
    public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public boolean isHidden() { return hidden; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
}
