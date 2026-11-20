package com.interviewiq.service;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.interviewiq.model.UserPreferences;
import com.interviewiq.repository.PreferencesRepository;
import com.interviewiq.util.ColorScheme;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

/**
 * Service managing application theme (Light / Dark), FlatLaf integration,
 * dynamic theme switching without restart, and local preference persistence.
 */
public class ThemeService {
    public interface ThemeChangeListener {
        void onThemeChanged(ColorScheme scheme);
    }

    private final PreferencesRepository preferencesRepository;
    private final List<ThemeChangeListener> listeners = new ArrayList<>();
    private boolean dark;
    private ColorScheme currentScheme;

    public ThemeService(PreferencesRepository preferencesRepository) {
        this.preferencesRepository = preferencesRepository;
        UserPreferences prefs = preferencesRepository.load();
        this.dark = "DARK".equalsIgnoreCase(prefs.getTheme());
        this.currentScheme = new ColorScheme(this.dark);
    }

    /**
     * Initializes FlatLaf Look and Feel on application startup based on saved preferences.
     */
    public void initializeLaf() {
        try {
            if (dark) {
                FlatDarkLaf.setup();
            } else {
                FlatLightLaf.setup();
            }
        } catch (Exception e) {
            System.err.println("Could not initialize FlatLaf: " + e.getMessage());
        }
    }

    public boolean isDark() {
        return dark;
    }

    public ColorScheme getCurrentScheme() {
        return currentScheme;
    }

    public void addThemeChangeListener(ThemeChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeThemeChangeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Toggles between Light and Dark mode dynamically across all open windows.
     */
    public void toggleTheme() {
        setDark(!dark);
    }

    /**
     * Sets the active theme, updates FlatLaf, updates all active frames/windows,
     * fires callbacks to custom components, and persists choice to preferences.
     */
    public void setDark(boolean isDark) {
        if (this.dark == isDark && currentScheme != null) {
            return;
        }

        this.dark = isDark;
        this.currentScheme = new ColorScheme(isDark);

        // Update FlatLaf Look and Feel
        try {
            if (isDark) {
                FlatDarkLaf.setup();
            } else {
                FlatLightLaf.setup();
            }
            FlatLaf.updateUI();
        } catch (Exception e) {
            System.err.println("Failed to update FlatLaf: " + e.getMessage());
        }

        // Notify registered custom listeners
        for (ThemeChangeListener listener : new ArrayList<>(listeners)) {
            try {
                listener.onThemeChanged(currentScheme);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Save preference
        UserPreferences prefs = preferencesRepository.load();
        prefs.setTheme(isDark ? "DARK" : "LIGHT");
        preferencesRepository.save(prefs);

        // Refresh all swing windows
        SwingUtilities.invokeLater(() -> {
            for (Window w : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(w);
                w.repaint();
            }
        });
    }
}
