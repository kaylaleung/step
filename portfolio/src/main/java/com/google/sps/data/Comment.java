package com.google.sps.data;

public final class Comment {
    
    private final String name;
    private final String id;
    private final String text;
    private final long timestamp;

    public Comment(String name, String id, String text, long timestamp) {
        this.name = name;
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
    }
}
