package org.jahia.modules.currentsapi.filters;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    public String getPublished() throws ParseException {
        String ds1 = this.published.substring(0,10);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("E, dd MMM yyyy");
        String ds2 = sdf2.format(sdf1.parse(ds1));
        return ds2;

    }
}