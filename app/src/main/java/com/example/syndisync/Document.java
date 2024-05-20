package com.example.syndisync;

public class Document {
    private String name;
    private String url;

    public Document(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}