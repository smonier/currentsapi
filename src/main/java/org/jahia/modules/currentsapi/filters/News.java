package org.jahia.modules.currentsapi.filters;

public class News {

    private String id;
    private String title;
    private String description;
    private String url;
    private String author;
    private String image;
    private String published;

    public News(String id,  String title, String description, String url, String author, String image, String published) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.author = author;
        this.image = image;
        this.published = published;

    }
    public String getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDescription() {
        return this.description;
    }
    public String getUrl() {
        return this.url;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getImage() {
        return this.image;
    }
    public String getPublished() {
        return this.published;
    }
}