package com.interviewiq.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.interviewiq.model.CodingChallenge;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.Question;
import com.interviewiq.model.QuestionState;
import com.interviewiq.model.QuizQuestion;
import com.interviewiq.util.AppConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for technical interview questions, MCQ quiz questions, and coding challenges.
 * Persists user state (bookmarks and understood questions) to JSON.
 */
public class QuestionRepository {
    private final Path statePath;
    private final ObjectMapper mapper;
    private final List<Question> questions = new ArrayList<>();
    private final List<QuizQuestion> quizQuestions = new ArrayList<>();
    private final List<CodingChallenge> codingChallenges = new ArrayList<>();
    private QuestionState questionState;

    public QuestionRepository() {
        this(resolveDefaultStatePath());
    }

    public QuestionRepository(Path customStatePath) {
        this.statePath = customStatePath;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        loadDefaultBank();
        loadState();
    }

    private static Path resolveDefaultStatePath() {
        Path dir = AppConstants.resolveDataDirectory();
        return dir.resolve(AppConstants.QUESTION_STATE_FILE);
    }

    private void loadDefaultBank() {
        this.questions.clear();
        this.questions.addAll(DefaultDataBank.createDefaultQuestions());
        this.quizQuestions.clear();
        this.quizQuestions.addAll(DefaultDataBank.createDefaultQuizQuestions());
        this.codingChallenges.clear();
        this.codingChallenges.addAll(DefaultDataBank.createDefaultCodingChallenges());
    }

    private synchronized void loadState() {
        File file = statePath.toFile();
        if (file.exists() && file.length() > 0) {
            try {
                this.questionState = mapper.readValue(file, QuestionState.class);
            } catch (Exception e) {
                System.err.println("Could not read question state, using empty state: " + e.getMessage());
                this.questionState = new QuestionState();
            }
        } else {
            this.questionState = new QuestionState();
        }

        // Apply state onto questions
        for (Question q : questions) {
            q.setBookmarked(questionState.getBookmarkedIds().contains(q.getId()));
            q.setUnderstood(questionState.getUnderstoodIds().contains(q.getId()));
        }
    }

    public synchronized void saveState() {
        try {
            File parent = statePath.toFile().getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            mapper.writeValue(statePath.toFile(), questionState);
        } catch (IOException e) {
            System.err.println("Failed to save question state: " + e.getMessage());
        }
    }

    public synchronized List<Question> getAllQuestions() {
        return new ArrayList<>(questions);
    }

    public synchronized List<QuizQuestion> getAllQuizQuestions() {
        return new ArrayList<>(quizQuestions);
    }

    public synchronized List<CodingChallenge> getAllCodingChallenges() {
        return new ArrayList<>(codingChallenges);
    }

    public synchronized Question getQuestionById(String id) {
        return questions.stream()
                .filter(q -> q.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public synchronized CodingChallenge getCodingChallengeById(String id) {
        return codingChallenges.stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public synchronized void setBookmarked(String questionId, boolean bookmarked) {
        Question q = getQuestionById(questionId);
        if (q != null) {
            q.setBookmarked(bookmarked);
            if (bookmarked) {
                questionState.getBookmarkedIds().add(questionId);
            } else {
                questionState.getBookmarkedIds().remove(questionId);
            }
            saveState();
        }
    }

    public synchronized void setUnderstood(String questionId, boolean understood) {
        Question q = getQuestionById(questionId);
        if (q != null) {
            q.setUnderstood(understood);
            if (understood) {
                questionState.getUnderstoodIds().add(questionId);
            } else {
                questionState.getUnderstoodIds().remove(questionId);
            }
            saveState();
        }
    }

    public synchronized void resetAllProgress() {
        questionState.getBookmarkedIds().clear();
        questionState.getUnderstoodIds().clear();
        for (Question q : questions) {
            q.setBookmarked(false);
            q.setUnderstood(false);
        }
        saveState();
    }
}
