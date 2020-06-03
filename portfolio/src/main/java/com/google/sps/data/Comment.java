package com.google.sps.data;

public final class Comment {
    
    private final String name;
    private final String text;
    private final long timestamp;

    public Comment(String name, String text, long timestamp) {
        this.name = name;
        this.text = text;
        this.timestamp = timestamp;
    }
}