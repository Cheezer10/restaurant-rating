package com.example.restaurantrating.entity;

public class Rating {
    private Long id;
    private Long visitorId;
    private Long restaurantId;
    private int rating;
    private String comment;

    public Rating() {}

    public Rating(Long id, Long visitorId, Long restaurantId, int rating, String comment) {
        this.id = id;
        this.visitorId = visitorId;
        this.restaurantId = restaurantId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVisitorId() { return visitorId; }
    public void setVisitorId(Long visitorId) { this.visitorId = visitorId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}