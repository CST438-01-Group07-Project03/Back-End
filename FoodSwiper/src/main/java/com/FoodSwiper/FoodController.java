package com.FoodSwiper;

import com.FoodSwiper.Entities.Food;
import com.FoodSwiper.Repositories.FoodRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@RestController
public class FoodController {
    private final FoodRepository repository;

    FoodController(FoodRepository repository){
        this.repository = repository;
    }

    // Get all foods
    @CrossOrigin
    @GetMapping("./food")
    List<Food> all(){
        return repository.findAll();
    }
    // Create a food
    @CrossOrigin
    @PostMapping("./food")
    Food newFood(@RequestBody Food newFood){
        return repository.save(newFood);
    }
    // Edit a food
    @CrossOrigin
    @PostMapping("./food/{id}")
    Food editFood(@PathVariable Long id, @RequestBody Food newFood){
        return repository.findById(id).map(food ->{
           return repository.save(food);
        }).orElseGet(()->{
            return  repository.save(newFood);
        });
    }
    // Edit a food
    @CrossOrigin
    @DeleteMapping("./food/{id}")
    void deleteFood(@PathVariable Long id){
        repository.deleteById(id);
    }
}
