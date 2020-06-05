package com.google.sps.data;

public class BlogPost {

    private final String title;
    private final String tag;
    private final String category;
    private final String blogpost;
    private final long timestamp;

    public BlogPost(String title, String tag, String category, String blogpost, long timestamp) {
        this.title = title;
        this.tag = tag;
        this.category = category;
        this.blogpost = blogpost;
        this.timestamp = timestamp;
    }
}