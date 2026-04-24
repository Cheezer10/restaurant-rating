package com.example.restaurantrating.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {
    @EmbeddedId
    private RatingId id;

    private int rating;
    private String comment;

    @ManyToOne
    @MapsId("visitorId")
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @ManyToOne
    @MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Rating() {}

    public Rating(RatingId id, int rating, String comment, Visitor visitor, Restaurant restaurant) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.visitor = visitor;
        this.restaurant = restaurant;
    }

    public RatingId getId() { return id; }
    public void setId(RatingId id) { this.id = id; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Visitor getVisitor() { return visitor; }
    public void setVisitor(Visitor visitor) { this.visitor = visitor; }
    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
}