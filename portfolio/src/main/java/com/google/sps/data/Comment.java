package com.google.sps.data;

public final class Comment {
    
    private final String name;
    private final String text;
    private final String tag;
    private final long timestamp;

    public Comment(String name, String text, String tag, long timestamp) {
        this.name = name;
        this.text = text;
        this.tag = tag;
        this.timestamp = timestamp;
    }
}
