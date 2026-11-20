package com.interviewiq.core;

import com.interviewiq.model.AssessmentType;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.Question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Core engine managing a simulated Mock Interview session with rule-based answer evaluation.
 */
public class InterviewEngine {
    private final String sessionId;
    private final String category;
    private final Difficulty difficulty;
    private final List<Question> questions;
    private final Map<Integer, String> candidateAnswers = new HashMap<>(); // questionIndex -> answerText
    private final Map<Integer, Integer> selfRatings = new HashMap<>(); // questionIndex -> rating (1-5)
    private final Set<Integer> flaggedQuestions = new HashSet<>();
    private final long totalTimeLimitSeconds;
    private long remainingSeconds;
    private int currentIndex = 0;
    private boolean completed = false;
    private long timeTakenSeconds = 0;

    public InterviewEngine(String category, Difficulty difficulty, List<Question> questionPool, int count, long timeLimitSeconds) {
        this.sessionId = UUID.randomUUID().toString();
        this.category = category;
        this.difficulty = difficulty;
        this.totalTimeLimitSeconds = timeLimitSeconds;
        this.remainingSeconds = timeLimitSeconds;

        List<Question> pool = new ArrayList<>();
        for (Question q : questionPool) {
            boolean matchesCat = category == null || category.equalsIgnoreCase("All Categories")
                    || category.equalsIgnoreCase("General Technical Interview")
                    || q.getCategory().equalsIgnoreCase(category);
            boolean matchesDiff = difficulty == null || difficulty == Difficulty.ALL || q.getDifficulty() == difficulty;
            if (matchesCat && matchesDiff) {
                pool.add(q);
            }
        }
        if (pool.isEmpty()) {
            pool.addAll(questionPool);
        }
        Collections.shuffle(pool);
        this.questions = new ArrayList<>(pool.subList(0, Math.min(count, pool.size())));
    }

    public String getSessionId() { return sessionId; }
    public String getCategory() { return category; }
    public Difficulty getDifficulty() { return difficulty; }
    public List<Question> getQuestions() { return Collections.unmodifiableList(questions); }
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

    public Question getCurrentQuestion() {
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

    public void saveAnswer(int index, String text) {
        if (text != null && !text.trim().isEmpty()) {
            candidateAnswers.put(index, text.trim());
        } else {
            candidateAnswers.remove(index);
        }
    }

    public String getAnswer(int index) {
        return candidateAnswers.getOrDefault(index, "");
    }

    public boolean isAnswered(int index) {
        String ans = candidateAnswers.get(index);
        return ans != null && !ans.trim().isEmpty();
    }

    public void setSelfRating(int index, int stars) {
        selfRatings.put(index, Math.max(1, Math.min(5, stars)));
    }

    public int getSelfRating(int index) {
        return selfRatings.getOrDefault(index, 3);
    }

    public void toggleFlag(int index) {
        if (flaggedQuestions.contains(index)) {
            flaggedQuestions.remove(index);
        } else {
            flaggedQuestions.add(index);
        }
    }

    public boolean isFlagged(int index) {
        return flaggedQuestions.contains(index);
    }

    public int getAnsweredCount() {
        return candidateAnswers.size();
    }

    /**
     * Rule-based evaluation of a candidate's answer against the question's model answer and tags.
     * Evaluates:
     * 1. Length & depth (at least 20 words = base points, >= 50 words = good depth).
     * 2. Keyword presence (technical terms extracted from tags and question title).
     * 3. Structural elements (bullet points, code indicators, or multiple paragraphs).
     * Returns a score between 0.0 and 100.0.
     */
    public double evaluateAnswer(int index) {
        if (!isAnswered(index)) return 0.0;
        String answer = candidateAnswers.get(index).toLowerCase(Locale.ROOT);
        Question q = questions.get(index);

        double score = 0.0;

        // 1. Length / Effort metric (up to 30 points)
        String[] words = answer.split("\\s+");
        if (words.length >= 15) score += 15.0;
        if (words.length >= 35) score += 10.0;
        if (words.length >= 60) score += 5.0;

        // 2. Keyword matching from tags & title (up to 50 points)
        List<String> keywords = new ArrayList<>();
        if (q.getTags() != null) {
            for (String tag : q.getTags()) {
                keywords.add(tag.toLowerCase(Locale.ROOT));
            }
        }
        for (String w : q.getTitle().toLowerCase(Locale.ROOT).split("[^a-zA-Z0-9]+")) {
            if (w.length() > 3) keywords.add(w);
        }

        int matchedKeywords = 0;
        for (String kw : keywords) {
            if (answer.contains(kw)) {
                matchedKeywords++;
            }
        }
        if (!keywords.isEmpty()) {
            double keywordRatio = Math.min(1.0, (double) matchedKeywords / Math.min(keywords.size(), 4));
            score += (keywordRatio * 50.0);
        } else {
            score += 35.0;
        }

        // 3. Structure & Formatting bonus (up to 20 points)
        if (answer.contains("1.") || answer.contains("•") || answer.contains("- ") || answer.contains("\n")) {
            score += 10.0; // structured points
        }
        if (answer.contains("for example") || answer.contains("e.g.") || answer.contains("such as") || answer.contains("//")) {
            score += 10.0; // concrete example provided
        }

        return Math.min(100.0, Math.round(score * 10.0) / 10.0);
    }

    public double calculateOverallScore() {
        if (questions.isEmpty()) return 0.0;
        double sum = 0.0;
        for (int i = 0; i < questions.size(); i++) {
            sum += evaluateAnswer(i);
        }
        return Math.round((sum / questions.size()) * 10.0) / 10.0;
    }

    public AttemptRecord submit() {
        this.completed = true;
        this.timeTakenSeconds = Math.max(1, totalTimeLimitSeconds - remainingSeconds);

        double overallScore = calculateOverallScore();
        int answered = getAnsweredCount();
        int total = questions.size();

        AttemptRecord record = new AttemptRecord(
                sessionId,
                AssessmentType.MOCK_INTERVIEW,
                "Mock Interview: " + category + " (" + difficulty.getDisplayName() + ")",
                category,
                difficulty,
                LocalDateTime.now(),
                timeTakenSeconds,
                total,
                answered,
                overallScore,
                100.0,
                overallScore,
                answered == total ? "COMPLETED" : "PARTIAL",
                ScoreCalculator.getReadinessLevel(overallScore)
        );

        Map<String, String> details = new HashMap<>();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            double qScore = evaluateAnswer(i);
            details.put("Q" + (i + 1) + "_title", q.getTitle());
            details.put("Q" + (i + 1) + "_score", String.valueOf(qScore));
            details.put("Q" + (i + 1) + "_selfRating", String.valueOf(getSelfRating(i)));
            details.put("Q" + (i + 1) + "_candidateAnswer", getAnswer(i));
            details.put("Q" + (i + 1) + "_modelAnswer", q.getAnswerText());
        }
        record.setDetails(details);
        return record;
    }

    public long getTimeTakenSeconds() {
        return timeTakenSeconds;
    }
}
