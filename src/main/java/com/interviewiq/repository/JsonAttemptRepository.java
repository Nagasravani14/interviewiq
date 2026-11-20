package com.interviewiq.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.util.AppConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Jackson JSON file implementation of AttemptRepository.
 */
public class JsonAttemptRepository implements AttemptRepository {
    private final Path storagePath;
    private final ObjectMapper mapper;
    private final List<AttemptRecord> cache = new ArrayList<>();
    private boolean loaded = false;

    public JsonAttemptRepository() {
        this(resolveDefaultPath());
    }

    public JsonAttemptRepository(Path customPath) {
        this.storagePath = customPath;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static Path resolveDefaultPath() {
        Path dir = AppConstants.resolveDataDirectory();
        return dir.resolve(AppConstants.HISTORY_FILE);
    }

    private synchronized void ensureLoaded() {
        if (loaded) return;
        cache.clear();
        File file = storagePath.toFile();
        if (file.exists() && file.length() > 0) {
            try {
                List<AttemptRecord> records = mapper.readValue(file, new TypeReference<List<AttemptRecord>>() {});
                if (records != null) {
                    cache.addAll(records);
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not parse attempt history: " + e.getMessage());
            }
        }
        loaded = true;
    }

    private synchronized void persist() {
        try {
            File parent = storagePath.toFile().getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            mapper.writeValue(storagePath.toFile(), cache);
        } catch (IOException e) {
            System.err.println("Failed to write attempt history: " + e.getMessage());
        }
    }

    @Override
    public synchronized List<AttemptRecord> findAll() {
        ensureLoaded();
        return new ArrayList<>(cache);
    }

    @Override
    public synchronized void save(AttemptRecord record) {
        if (record == null) return;
        ensureLoaded();
        cache.removeIf(r -> r.getId().equals(record.getId()));
        cache.add(record);
        persist();
    }

    @Override
    public synchronized void saveAll(List<AttemptRecord> records) {
        if (records == null) return;
        ensureLoaded();
        for (AttemptRecord r : records) {
            cache.removeIf(existing -> existing.getId().equals(r.getId()));
            cache.add(r);
        }
        persist();
    }

    @Override
    public synchronized void deleteById(String id) {
        if (id == null) return;
        ensureLoaded();
        if (cache.removeIf(r -> r.getId().equals(id))) {
            persist();
        }
    }

    @Override
    public synchronized void clearAll() {
        ensureLoaded();
        cache.clear();
        persist();
    }

    public Path getStoragePath() {
        return storagePath;
    }
}
