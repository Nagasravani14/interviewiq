package com.interviewiq.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.interviewiq.model.UserPreferences;
import com.interviewiq.util.AppConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles persistent storage of UserPreferences in JSON.
 */
public class PreferencesRepository {
    private final Path storagePath;
    private final ObjectMapper mapper;
    private UserPreferences cachedPreferences;

    public PreferencesRepository() {
        this(resolveDefaultPath());
    }

    public PreferencesRepository(Path customPath) {
        this.storagePath = customPath;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static Path resolveDefaultPath() {
        Path dir = AppConstants.resolveDataDirectory();
        return dir.resolve(AppConstants.PREFERENCES_FILE);
    }

    public synchronized UserPreferences load() {
        if (cachedPreferences != null) {
            return cachedPreferences;
        }

        File file = storagePath.toFile();
        if (!file.exists() || file.length() == 0) {
            cachedPreferences = new UserPreferences();
            save(cachedPreferences);
            return cachedPreferences;
        }

        try {
            cachedPreferences = mapper.readValue(file, UserPreferences.class);
            if (cachedPreferences == null) {
                cachedPreferences = new UserPreferences();
            }
        } catch (Exception e) {
            System.err.println("Failed to read user preferences, falling back to default: " + e.getMessage());
            cachedPreferences = new UserPreferences();
        }
        return cachedPreferences;
    }

    public synchronized void save(UserPreferences preferences) {
        if (preferences == null) return;
        this.cachedPreferences = preferences;
        try {
            File parent = storagePath.toFile().getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            mapper.writeValue(storagePath.toFile(), preferences);
        } catch (IOException e) {
            System.err.println("Failed to save user preferences: " + e.getMessage());
        }
    }

    public Path getStoragePath() {
        return storagePath;
    }
}
