package com.example.restaurantfinder.model;

public class SearchResponse {

    private String name;
    private String rating;
    private String ratingColor;
    private String contact;
    private String locality;
    private String image;
    private String cuisines;
    private String url;
    private String latitude;
    private String longitude;

    SearchResponse(String name, String rating, String ratingColor, String contact,
                   String locality, String image, String cuisines, String url, String latitude, String longitude) {
        this.name = name;
        this.rating = rating;
        this.ratingColor = ratingColor;
        this.contact = contact;
        this.locality = locality;
        this.image = image;
        this.cuisines = cuisines;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public String getContact() {
        return contact;
    }

    public String getLocality() {
        return locality;
    }

    public String getImage() {
        return image;
    }

    public String getCuisines() {
        return cuisines;
    }

    public String getUrl() {
        return url;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
