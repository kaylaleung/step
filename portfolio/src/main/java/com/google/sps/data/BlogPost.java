package com.google.sps.data;

public class BlogPost {

    private final String title;
    private final String blogpost;
    private final long timestamp;

    public BlogPost(String title, String blogpost, long timestamp) {
        this.title = title;
        this.blogpost = blogpost;
        this.timestamp = timestamp;
    }
}