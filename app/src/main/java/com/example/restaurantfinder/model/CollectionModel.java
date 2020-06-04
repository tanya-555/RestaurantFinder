package com.example.restaurantfinder.model;

public class CollectionModel {

    private String title;

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    private String imageUrl;
    private String description;

    public CollectionModel(String title, String imageUrl, String description) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
