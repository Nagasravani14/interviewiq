package com.interviewiq.repository;

import com.interviewiq.model.AttemptRecord;
import java.util.List;

/**
 * Data access interface for assessment attempt history.
 */
public interface AttemptRepository {
    List<AttemptRecord> findAll();
    void save(AttemptRecord record);
    void saveAll(List<AttemptRecord> records);
    void deleteById(String id);
    void clearAll();
}
