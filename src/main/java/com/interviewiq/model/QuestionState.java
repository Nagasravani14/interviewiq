package com.interviewiq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;

/**
 * Persisted state of bookmarks and understood status for question bank.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionState {
    private Set<String> bookmarkedIds = new HashSet<>();
    private Set<String> understoodIds = new HashSet<>();

    public QuestionState() {}

    public Set<String> getBookmarkedIds() { return bookmarkedIds; }
    public void setBookmarkedIds(Set<String> bookmarkedIds) {
        this.bookmarkedIds = bookmarkedIds != null ? bookmarkedIds : new HashSet<>();
    }

    public Set<String> getUnderstoodIds() { return understoodIds; }
    public void setUnderstoodIds(Set<String> understoodIds) {
        this.understoodIds = understoodIds != null ? understoodIds : new HashSet<>();
    }
}
