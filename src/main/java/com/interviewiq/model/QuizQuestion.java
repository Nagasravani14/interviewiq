package com.interviewiq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Multiple-choice question for Quiz Arena.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizQuestion {
    private String id;
    private String category;
    private Difficulty difficulty;
    private String questionText;
    private List<String> options = new ArrayList<>();
    private int correctOptionIndex; // 0-based index
    private String explanation;

    public QuizQuestion() {}

    public QuizQuestion(String id, String category, Difficulty difficulty,
                        String questionText, List<String> options,
                        int correctOptionIndex, String explanation) {
        this.id = id;
        this.category = category;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.options = options != null ? options : new ArrayList<>();
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
