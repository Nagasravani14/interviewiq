package com.interviewiq.service;

import com.interviewiq.model.CodingChallenge;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.Question;
import com.interviewiq.model.QuizQuestion;
import com.interviewiq.model.TopicProgress;
import com.interviewiq.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service managing questions, search filters, bookmarks, and topic progress.
 */
public class QuestionService {
    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> getAllQuestions() {
        return repository.getAllQuestions();
    }

    public List<QuizQuestion> getAllQuizQuestions() {
        return repository.getAllQuizQuestions();
    }

    public List<CodingChallenge> getAllCodingChallenges() {
        return repository.getAllCodingChallenges();
    }

    public Question getQuestionById(String id) {
        return repository.getQuestionById(id);
    }

    public CodingChallenge getCodingChallengeById(String id) {
        return repository.getCodingChallengeById(id);
    }

    public void toggleBookmark(String questionId) {
        Question q = repository.getQuestionById(questionId);
        if (q != null) {
            repository.setBookmarked(questionId, !q.isBookmarked());
        }
    }

    public void toggleUnderstood(String questionId) {
        Question q = repository.getQuestionById(questionId);
        if (q != null) {
            repository.setUnderstood(questionId, !q.isUnderstood());
        }
    }

    public List<Question> filterQuestions(String category, Difficulty difficulty, String searchQuery, String statusFilter) {
        return repository.getAllQuestions().stream()
                .filter(q -> {
                    // Category filter
                    if (category != null && !category.equalsIgnoreCase("All Categories") && !category.equalsIgnoreCase("All")) {
                        if (!q.getCategory().equalsIgnoreCase(category)) return false;
                    }
                    // Difficulty filter
                    if (difficulty != null && difficulty != Difficulty.ALL) {
                        if (q.getDifficulty() != difficulty) return false;
                    }
                    // Status filter: "All", "Bookmarked", "Understood", "Unread"
                    if (statusFilter != null && !statusFilter.equalsIgnoreCase("All")) {
                        if (statusFilter.equalsIgnoreCase("Bookmarked") && !q.isBookmarked()) return false;
                        if (statusFilter.equalsIgnoreCase("Understood") && !q.isUnderstood()) return false;
                        if (statusFilter.equalsIgnoreCase("Unread") && q.isUnderstood()) return false;
                    }
                    // Search query
                    if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                        String query = searchQuery.trim().toLowerCase(Locale.ROOT);
                        boolean matchesTitle = q.getTitle().toLowerCase(Locale.ROOT).contains(query);
                        boolean matchesText = q.getQuestionText().toLowerCase(Locale.ROOT).contains(query);
                        boolean matchesTags = q.getTags().stream().anyMatch(t -> t.toLowerCase(Locale.ROOT).contains(query));
                        if (!matchesTitle && !matchesText && !matchesTags) return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public TopicProgress getTopicProgress(String category) {
        List<Question> topicQuestions = repository.getAllQuestions().stream()
                .filter(q -> category == null || category.equalsIgnoreCase("All Categories") || q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        int total = topicQuestions.size();
        int understood = (int) topicQuestions.stream().filter(Question::isUnderstood).count();
        int bookmarked = (int) topicQuestions.stream().filter(Question::isBookmarked).count();
        double completionRate = total > 0 ? ((double) understood / total) * 100.0 : 0.0;

        return new TopicProgress(category != null ? category : "All", total, understood, bookmarked, completionRate);
    }

    public Map<String, TopicProgress> getAllTopicProgress() {
        Map<String, TopicProgress> map = new HashMap<>();
        List<Question> all = repository.getAllQuestions();
        Map<String, List<Question>> byCategory = all.stream().collect(Collectors.groupingBy(Question::getCategory));

        for (Map.Entry<String, List<Question>> entry : byCategory.entrySet()) {
            String cat = entry.getKey();
            List<Question> list = entry.getValue();
            int total = list.size();
            int understood = (int) list.stream().filter(Question::isUnderstood).count();
            int bookmarked = (int) list.stream().filter(Question::isBookmarked).count();
            double rate = total > 0 ? ((double) understood / total) * 100.0 : 0.0;
            map.put(cat, new TopicProgress(cat, total, understood, bookmarked, rate));
        }
        return map;
    }

    public List<String> getDistinctCategories() {
        return repository.getAllQuestions().stream()
                .map(Question::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public void resetAllProgress() {
        repository.resetAllProgress();
    }
}
