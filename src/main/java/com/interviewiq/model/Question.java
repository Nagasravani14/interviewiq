package com.interviewiq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Technical question for Interview Prep and Practice Arena.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {
    private String id;
    private String title;
    private String category;
    private Difficulty difficulty;
    private List<String> tags = new ArrayList<>();
    private String questionText;
    private String answerText;
    private String explanationText;
    private String codeExample;
    private boolean bookmarked;
    private boolean understood;

    public Question() {}

    public Question(String id, String title, String category, Difficulty difficulty,
                    List<String> tags, String questionText, String answerText,
                    String explanationText, String codeExample) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.questionText = questionText;
        this.answerText = answerText;
        this.explanationText = explanationText;
        this.codeExample = codeExample;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public String getExplanationText() { return explanationText; }
    public void setExplanationText(String explanationText) { this.explanationText = explanationText; }

    public String getCodeExample() { return codeExample; }
    public void setCodeExample(String codeExample) { this.codeExample = codeExample; }

    public boolean isBookmarked() { return bookmarked; }
    public void setBookmarked(boolean bookmarked) { this.bookmarked = bookmarked; }

    public boolean isUnderstood() { return understood; }
    public void setUnderstood(boolean understood) { this.understood = understood; }
}
