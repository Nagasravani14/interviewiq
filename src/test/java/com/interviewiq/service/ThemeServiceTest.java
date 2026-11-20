package com.interviewiq.service;

import com.interviewiq.model.UserPreferences;
import com.interviewiq.repository.PreferencesRepository;
import com.interviewiq.util.ColorScheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ThemeServiceTest {

    @Test
    @DisplayName("Test ThemeService switching, listener callback, and preference persistence")
    void testThemeSwitchingAndPersistence(@TempDir Path tempDir) {
        Path prefsFile = tempDir.resolve("prefs_test.json");
        PreferencesRepository repo = new PreferencesRepository(prefsFile);

        ThemeService themeService = new ThemeService(repo);
        assertFalse(themeService.isDark(), "Default theme should be Light");
        assertNotNull(themeService.getCurrentScheme());

        AtomicBoolean listenerFired = new AtomicBoolean(false);
        AtomicReference<ColorScheme> lastScheme = new AtomicReference<>();
        ThemeService.ThemeChangeListener listener = scheme -> {
            listenerFired.set(true);
            lastScheme.set(scheme);
        };
        themeService.addThemeChangeListener(listener);

        // Switch to Dark
        themeService.setDark(true);
        assertTrue(themeService.isDark());
        assertTrue(listenerFired.get(), "Listener should have been notified of theme change");
        assertNotNull(lastScheme.get());
        assertTrue(lastScheme.get().isDark());

        // Verify persisted to file
        PreferencesRepository verifyRepo = new PreferencesRepository(prefsFile);
        UserPreferences saved = verifyRepo.load();
        assertEquals("DARK", saved.getTheme());

        // Toggle back to Light
        themeService.toggleTheme();
        assertFalse(themeService.isDark());
        assertFalse(lastScheme.get().isDark());
        assertEquals("LIGHT", new PreferencesRepository(prefsFile).load().getTheme());
    }
}
