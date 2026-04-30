package com.FoodSwiper;

import com.FoodSwiper.Entities.Restaurant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void constructorSetsFieldsCorrectly() {
        Restaurant r = new Restaurant("Lure Fish House", "yelp123", "403 State St",
                "Santa Barbara", "CA", "93101", 4.5, 800, "Restaurants, Seafood", "http://img.url");

        assertEquals("Lure Fish House", r.getName());
        assertEquals("yelp123", r.getYelpId());
        assertEquals("403 State St", r.getAddress());
        assertEquals("Santa Barbara", r.getCity());
        assertEquals("CA", r.getState());
        assertEquals("93101", r.getZipCode());
        assertEquals(4.5, r.getStars());
        assertEquals(800, r.getReviewCount());
        assertEquals("Restaurants, Seafood", r.getCategories());
        assertEquals("http://img.url", r.getImageUrl());
    }

    @Test
    void defaultConstructorCreatesEmptyRestaurant() {
        Restaurant r = new Restaurant();
        assertNotNull(r);
        assertEquals(0.0, r.getStars());
        assertEquals(0, r.getReviewCount());
    }

    @Test
    void settersUpdateFields() {
        Restaurant r = new Restaurant();
        r.setYelpId("abc");
        r.setCity("Santa Barbara");
        r.setStars(3.5);

        assertEquals("abc", r.getYelpId());
        assertEquals("Santa Barbara", r.getCity());
        assertEquals(3.5, r.getStars());
    }
}