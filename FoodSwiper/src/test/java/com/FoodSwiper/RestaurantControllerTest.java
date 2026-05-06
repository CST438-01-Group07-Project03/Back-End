package com.FoodSwiper;

import com.FoodSwiper.Entities.Restaurant;
import com.FoodSwiper.Repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantControllerTest {

    RestaurantRepository repository;
    RestaurantController controller;

    @BeforeEach
    void setUp() {
        repository = mock(RestaurantRepository.class);
        controller = new RestaurantController(repository);
    }

    @Test
    void getAllReturnsAllRestaurants() {
        Restaurant r = new Restaurant("Lure Fish House", "yelp123", "403 State St",
                "Santa Barbara", "CA", "93101", 4.5, 800, "Restaurants, Seafood", "");
        when(repository.findAll()).thenReturn(List.of(r));

        List<Restaurant> result = controller.all();

        assertEquals(1, result.size());
        assertEquals("Lure Fish House", result.get(0).getName());
    }

    @Test
    void createRestaurantSavesAndReturns() {
        Restaurant r = new Restaurant("Lure Fish House", "yelp123", "403 State St",
                "Santa Barbara", "CA", "93101", 4.5, 800, "Restaurants, Seafood", "");
        when(repository.save(any(Restaurant.class))).thenReturn(r);

        Restaurant result = controller.newRestaurant(r);

        assertEquals("Lure Fish House", result.getName());
        verify(repository, times(1)).save(r);
    }

    @Test
    void deleteRestaurantCallsRepository() {
        controller.deleteRestaurant(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void editRestaurantReturnsUpdatedWhenFound() {
        Restaurant existing = new Restaurant("Old Name", "yelp123", "403 State St",
                "Santa Barbara", "CA", "93101", 4.5, 800, "Restaurants", "");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Restaurant.class))).thenReturn(existing);

        Restaurant result = controller.editRestaurant(1L, existing);

        assertNotNull(result);
        verify(repository, times(1)).save(any());
    }
}