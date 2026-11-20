package com.interviewiq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User settings and preferences model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPreferences {
    private String displayName = "Alex Johnson";
    private String targetRole = "Full Stack Java Engineer";
    private int dailyQuestionGoal = 5;
    private String theme = "LIGHT";
    private Difficulty defaultDifficulty = Difficulty.MEDIUM;
    private int defaultQuizLength = 10;
    private int defaultTimerMinutes = 15;
    private boolean timerEnabled = true;
    private boolean demoDataLoaded = false;

    public UserPreferences() {}

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }

    public int getDailyQuestionGoal() { return dailyQuestionGoal; }
    public void setDailyQuestionGoal(int dailyQuestionGoal) { this.dailyQuestionGoal = dailyQuestionGoal; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public Difficulty getDefaultDifficulty() { return defaultDifficulty; }
    public void setDefaultDifficulty(Difficulty defaultDifficulty) { this.defaultDifficulty = defaultDifficulty; }

    public int getDefaultQuizLength() { return defaultQuizLength; }
    public void setDefaultQuizLength(int defaultQuizLength) { this.defaultQuizLength = defaultQuizLength; }

    public int getDefaultTimerMinutes() { return defaultTimerMinutes; }
    public void setDefaultTimerMinutes(int defaultTimerMinutes) { this.defaultTimerMinutes = defaultTimerMinutes; }

    public boolean isTimerEnabled() { return timerEnabled; }
    public void setTimerEnabled(boolean timerEnabled) { this.timerEnabled = timerEnabled; }

    public boolean isDemoDataLoaded() { return demoDataLoaded; }
    public void setDemoDataLoaded(boolean demoDataLoaded) { this.demoDataLoaded = demoDataLoaded; }
}
