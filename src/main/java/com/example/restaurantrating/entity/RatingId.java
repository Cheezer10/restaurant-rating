package com.example.restaurantrating.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RatingId implements Serializable {
    private Long visitorId;
    private Long restaurantId;

    public RatingId() {}

    public RatingId(Long visitorId, Long restaurantId) {
        this.visitorId = visitorId;
        this.restaurantId = restaurantId;
    }

    public Long getVisitorId() { return visitorId; }
    public void setVisitorId(Long visitorId) { this.visitorId = visitorId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return Objects.equals(visitorId, ratingId.visitorId) &&
                Objects.equals(restaurantId, ratingId.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitorId, restaurantId);
    }
}