package com.interviewiq.core;

import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.AssessmentType;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.QuizQuestion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Core engine managing an active MCQ Quiz session.
 */
public class QuizEngine {
    private final String sessionId;
    private final String category;
    private final Difficulty difficulty;
    private final List<QuizQuestion> questions;
    private final Map<Integer, Integer> answers = new HashMap<>(); // questionIndex -> selectedOptionIndex
    private final Set<Integer> flaggedQuestions = new HashSet<>(); // questionIndex
    private final long totalTimeLimitSeconds;
    private long remainingSeconds;
    private int currentIndex = 0;
    private boolean completed = false;
    private long timeTakenSeconds = 0;

    public QuizEngine(String category, Difficulty difficulty, List<QuizQuestion> questionPool, int count, long timeLimitSeconds) {
        this.sessionId = UUID.randomUUID().toString();
        this.category = category;
        this.difficulty = difficulty;
        this.totalTimeLimitSeconds = timeLimitSeconds;
        this.remainingSeconds = timeLimitSeconds;

        // Select and shuffle questions
        List<QuizQuestion> filtered = new ArrayList<>();
        for (QuizQuestion q : questionPool) {
            boolean matchesCat = category == null || category.equalsIgnoreCase("All Categories") || q.getCategory().equalsIgnoreCase(category);
            boolean matchesDiff = difficulty == null || difficulty == Difficulty.ALL || q.getDifficulty() == difficulty;
            if (matchesCat && matchesDiff) {
                filtered.add(q);
            }
        }
        if (filtered.isEmpty()) {
            // fallback to any
            filtered.addAll(questionPool);
        }
        Collections.shuffle(filtered);
        this.questions = new ArrayList<>(filtered.subList(0, Math.min(count, filtered.size())));
    }

    public String getSessionId() { return sessionId; }
    public String getCategory() { return category; }
    public Difficulty getDifficulty() { return difficulty; }
    public List<QuizQuestion> getQuestions() { return Collections.unmodifiableList(questions); }
    public int getTotalQuestions() { return questions.size(); }
    public int getCurrentIndex() { return currentIndex; }
    public long getTotalTimeLimitSeconds() { return totalTimeLimitSeconds; }
    public long getRemainingSeconds() { return remainingSeconds; }
    public boolean isCompleted() { return completed; }

    public void setRemainingSeconds(long seconds) {
        this.remainingSeconds = Math.max(0, seconds);
    }

    public void decrementTimer() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
        }
    }

    public QuizQuestion getCurrentQuestion() {
        if (questions.isEmpty() || currentIndex < 0 || currentIndex >= questions.size()) {
            return null;
        }
        return questions.get(currentIndex);
    }

    public boolean hasNext() {
        return currentIndex < questions.size() - 1;
    }

    public boolean hasPrevious() {
        return currentIndex > 0;
    }

    public void next() {
        if (hasNext()) currentIndex++;
    }

    public void previous() {
        if (hasPrevious()) currentIndex--;
    }

    public void jumpTo(int index) {
        if (index >= 0 && index < questions.size()) {
            this.currentIndex = index;
        }
    }

    public void selectAnswer(int optionIndex) {
        answers.put(currentIndex, optionIndex);
    }

    public void clearAnswer() {
        answers.remove(currentIndex);
    }

    public Integer getSelectedAnswer(int questionIndex) {
        return answers.get(questionIndex);
    }

    public boolean isAnswered(int questionIndex) {
        return answers.containsKey(questionIndex);
    }

    public void toggleFlag(int questionIndex) {
        if (flaggedQuestions.contains(questionIndex)) {
            flaggedQuestions.remove(questionIndex);
        } else {
            flaggedQuestions.add(questionIndex);
        }
    }

    public boolean isFlagged(int questionIndex) {
        return flaggedQuestions.contains(questionIndex);
    }

    public int getAnsweredCount() {
        return answers.size();
    }

    public int getUnansweredCount() {
        return questions.size() - answers.size();
    }

    public int getFlaggedCount() {
        return flaggedQuestions.size();
    }

    public int calculateCorrectCount() {
        int correct = 0;
        for (int i = 0; i < questions.size(); i++) {
            Integer chosen = answers.get(i);
            if (chosen != null && chosen == questions.get(i).getCorrectOptionIndex()) {
                correct++;
            }
        }
        return correct;
    }

    public double calculateAccuracy() {
        return ScoreCalculator.calculateAccuracy(calculateCorrectCount(), questions.size());
    }

    public AttemptRecord submit() {
        this.completed = true;
        this.timeTakenSeconds = Math.max(1, totalTimeLimitSeconds - remainingSeconds);

        int correct = calculateCorrectCount();
        int total = questions.size();
        double accuracy = calculateAccuracy();

        AttemptRecord record = new AttemptRecord(
                sessionId,
                AssessmentType.QUIZ,
                "Quiz: " + category + " (" + difficulty.getDisplayName() + ")",
                category,
                difficulty,
                LocalDateTime.now(),
                timeTakenSeconds,
                total,
                answers.size(),
                correct,
                total,
                accuracy,
                "COMPLETED",
                ScoreCalculator.getGrade(accuracy)
        );

        // Store question-by-question breakdown in details
        Map<String, String> details = new HashMap<>();
        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion q = questions.get(i);
            Integer userChoice = answers.get(i);
            boolean isCorrect = userChoice != null && userChoice == q.getCorrectOptionIndex();
            details.put("Q" + (i + 1) + "_status", isCorrect ? "CORRECT" : (userChoice == null ? "UNANSWERED" : "INCORRECT"));
            details.put("Q" + (i + 1) + "_text", q.getQuestionText());
            details.put("Q" + (i + 1) + "_userChoice", userChoice != null ? q.getOptions().get(userChoice) : "None");
            details.put("Q" + (i + 1) + "_correctChoice", q.getOptions().get(q.getCorrectOptionIndex()));
            details.put("Q" + (i + 1) + "_explanation", q.getExplanation());
        }
        record.setDetails(details);
        return record;
    }

    public long getTimeTakenSeconds() {
        return timeTakenSeconds;
    }
}
