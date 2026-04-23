package com.FoodSwiper;

import com.FoodSwiper.Entities.Restaurant;
import com.FoodSwiper.Repositories.RestaurantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@RestController
public class RestaurantController {
    private final RestaurantRepository repository;

    RestaurantController(RestaurantRepository repository){
        this.repository = repository;
    }

    // Get all restaurants
    @CrossOrigin
    @GetMapping("./restaurants")
    List<Restaurant> all(){
        return repository.findAll();
    }
    // Create a restaurant
    @CrossOrigin
    @PostMapping("./restaurants")
    Restaurant newRestaurant(@RequestBody Restaurant newRestaurant){
        return repository.save(newRestaurant);
    }
    // Edit a restaurant
    @CrossOrigin
    @PutMapping("./restaurants/{id}")
    Restaurant editRestaurant(@PathVariable Long id, @RequestBody Restaurant newRestaurant){
        return repository.findById(id).map(restaurant -> {
            return repository.save(restaurant);
        }).orElseGet(()->{
            return repository.save(newRestaurant);
        });
    }
    // Delete a restaurant
    @CrossOrigin
    @DeleteMapping("./restaurants/{id}")
    void deleteRestaurant(@PathVariable Long id){
        repository.deleteById(id);
    }
}
