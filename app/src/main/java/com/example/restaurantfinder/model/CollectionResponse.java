package com.example.restaurantfinder.model;

public class CollectionResponse {

    private String title;
    private int collectionId;
    private String imageUrl;
    private String description;

    public CollectionResponse(String title, String imageUrl, String description, int collectionId) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.collectionId = collectionId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getCollectionId() {
        return collectionId;
    }
}
