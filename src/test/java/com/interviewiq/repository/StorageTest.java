package com.interviewiq.repository;

import com.interviewiq.model.AssessmentType;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.UserPreferences;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {

    @Test
    @DisplayName("Test PreferencesRepository save, reload, and default values")
    void testPreferencesStorage(@TempDir Path tempDir) {
        Path path = tempDir.resolve("user_preferences.json");
        PreferencesRepository repo = new PreferencesRepository(path);

        UserPreferences p = repo.load();
        assertEquals("Alex Johnson", p.getDisplayName());
        assertEquals("LIGHT", p.getTheme());

        p.setDisplayName("Sarah Connor");
        p.setTheme("DARK");
        p.setDailyQuestionGoal(8);
        repo.save(p);

        PreferencesRepository reloaded = new PreferencesRepository(path);
        UserPreferences p2 = reloaded.load();
        assertEquals("Sarah Connor", p2.getDisplayName());
        assertEquals("DARK", p2.getTheme());
        assertEquals(8, p2.getDailyQuestionGoal());
    }

    @Test
    @DisplayName("Test JsonAttemptRepository save, query, delete, and clear")
    void testAttemptRepositoryStorage(@TempDir Path tempDir) {
        Path path = tempDir.resolve("attempt_history.json");
        JsonAttemptRepository repo = new JsonAttemptRepository(path);

        assertTrue(repo.findAll().isEmpty());

        AttemptRecord record1 = new AttemptRecord("att-1", AssessmentType.QUIZ, "Java Quiz", "Java Programming",
                Difficulty.EASY, LocalDateTime.now(), 60, 5, 5, 5.0, 5.0, 100.0, "COMPLETED", "Perfect");
        AttemptRecord record2 = new AttemptRecord("att-2", AssessmentType.CODING_ASSESSMENT, "Two Sum", "DSA",
                Difficulty.MEDIUM, LocalDateTime.now(), 180, 4, 4, 4.0, 4.0, 100.0, "COMPLETED", "Pass");

        repo.save(record1);
        repo.save(record2);

        assertEquals(2, repo.findAll().size());

        // Verify reloaded from disk
        JsonAttemptRepository reloaded = new JsonAttemptRepository(path);
        List<AttemptRecord> list = reloaded.findAll();
        assertEquals(2, list.size());
        assertEquals("att-1", list.get(0).getId());

        // Delete one
        reloaded.deleteById("att-1");
        assertEquals(1, reloaded.findAll().size());
        assertEquals("att-2", reloaded.findAll().get(0).getId());

        // Clear all
        reloaded.clearAll();
        assertTrue(reloaded.findAll().isEmpty());
    }

    @Test
    @DisplayName("Test QuestionRepository bookmarks, understood state, and persistence")
    void testQuestionStateStorage(@TempDir Path tempDir) {
        Path statePath = tempDir.resolve("question_state.json");
        QuestionRepository repo = new QuestionRepository(statePath);

        assertNotNull(repo.getAllQuestions());
        assertFalse(repo.getAllQuestions().isEmpty());

        String qId = repo.getAllQuestions().get(0).getId();

        repo.setBookmarked(qId, true);
        repo.setUnderstood(qId, true);

        assertTrue(repo.getQuestionById(qId).isBookmarked());
        assertTrue(repo.getQuestionById(qId).isUnderstood());

        // Reload from disk to verify persistence
        QuestionRepository reloaded = new QuestionRepository(statePath);
        assertTrue(reloaded.getQuestionById(qId).isBookmarked());
        assertTrue(reloaded.getQuestionById(qId).isUnderstood());

        reloaded.resetAllProgress();
        assertFalse(reloaded.getQuestionById(qId).isBookmarked());
        assertFalse(reloaded.getQuestionById(qId).isUnderstood());
    }

    @Test
    @DisplayName("Test graceful recovery from corrupted JSON files")
    void testCorruptedJsonRecovery(@TempDir Path tempDir) throws IOException {
        Path corruptPrefs = tempDir.resolve("corrupt_prefs.json");
        Files.writeString(corruptPrefs, "{ this is not valid json ### !!! }");

        PreferencesRepository repo = new PreferencesRepository(corruptPrefs);
        UserPreferences p = repo.load();
        assertNotNull(p, "Should return default UserPreferences instead of throwing unhandled exception");
        assertEquals("Alex Johnson", p.getDisplayName());
    }
}
