package com.interviewiq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a coding problem in Coding Assessment.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodingChallenge {
    private String id;
    private String title;
    private String category;
    private Difficulty difficulty;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String constraints;
    private String starterCode;
    private String solutionApproach;
    private List<TestCase> testCases = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private int scoreWeight = 100;

    public CodingChallenge() {}

    public CodingChallenge(String id, String title, String category, Difficulty difficulty,
                           String description, String inputFormat, String outputFormat,
                           String constraints, String starterCode, String solutionApproach,
                           List<TestCase> testCases, List<String> tags) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.description = description;
        this.inputFormat = inputFormat;
        this.outputFormat = outputFormat;
        this.constraints = constraints;
        this.starterCode = starterCode;
        this.solutionApproach = solutionApproach;
        this.testCases = testCases != null ? testCases : new ArrayList<>();
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInputFormat() { return inputFormat; }
    public void setInputFormat(String inputFormat) { this.inputFormat = inputFormat; }

    public String getOutputFormat() { return outputFormat; }
    public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }

    public String getConstraints() { return constraints; }
    public void setConstraints(String constraints) { this.constraints = constraints; }

    public String getStarterCode() { return starterCode; }
    public void setStarterCode(String starterCode) { this.starterCode = starterCode; }

    public String getSolutionApproach() { return solutionApproach; }
    public void setSolutionApproach(String solutionApproach) { this.solutionApproach = solutionApproach; }

    public List<TestCase> getTestCases() { return testCases; }
    public void setTestCases(List<TestCase> testCases) { this.testCases = testCases; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public int getScoreWeight() { return scoreWeight; }
    public void setScoreWeight(int scoreWeight) { this.scoreWeight = scoreWeight; }
}
