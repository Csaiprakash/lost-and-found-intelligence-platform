package com.lostfound.MatchingService.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MatchingReportCache implements Serializable {

    private static final long serialVersionUID = 1L;

    private String text;
    private Map<String, Object> metadata;

    public MatchingReportCache() {
    }

    public MatchingReportCache(
            String text,
            Map<String, Object> metadata) {

        this.text = text;
        this.metadata = metadata == null
                ? new HashMap<>()
                : new HashMap<>(metadata);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
