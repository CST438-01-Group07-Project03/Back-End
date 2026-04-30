package com.FoodSwiper;

import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Repositories.ItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/29/2026
 */
@RestController
public class ItemController {
    private final ItemRepository repository;
    ItemController(ItemRepository repository){
        this.repository = repository;
    }

    // Show all items
    @CrossOrigin
    @GetMapping("/items")
    List<Item> getAll(){
        return repository.findAll();
    }
    // Get item by id
    @CrossOrigin
    @GetMapping("/items/{id}")
    Item getItem(@PathVariable Long id){
        return repository.findById(id).orElse(null);
    }
    // Create a new item
    @CrossOrigin
    @PostMapping("/items")
    Item newItem(@RequestBody Item newItem){
        return repository.save(newItem);
    }
    // Edit an item
    @CrossOrigin
    @PutMapping("/items/{id}")
    Item editItem(@PathVariable Long id, @RequestBody Item newItem){
        return repository.findById(id).map(item -> {
            return repository.save(item);
        }).orElseGet(() -> {
            return repository.save(newItem);
        });
    }
    // Delete an item
    @CrossOrigin
    @DeleteMapping("/items/{id}")
    void deleteItem(@PathVariable Long id){
        repository.deleteById(id);
    }
}
