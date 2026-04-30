package com.FoodSwiper;

import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.ItemRepository;
import com.FoodSwiper.Repositories.UsersRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@RestController
class UsersController {

    private final UsersRepository repository;
    private final ItemRepository itemRepository;
    UsersController(UsersRepository repository, ItemRepository itemRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
    }

    // Get all users
    @CrossOrigin
    @GetMapping("/users")
    List<Users> all() {
        return repository.findAll();
    }

    // Get a user by id
    @CrossOrigin
    @GetMapping("/users/{id}")
    Users getUser(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // Create a user
    @CrossOrigin
    @PostMapping("/users")
    Users newUser(@RequestBody Users newUser) {
        return repository.save(newUser);
    }

    // Edit a user
    @CrossOrigin
    @PutMapping("/users/{id}")
    Users editUser(@PathVariable Long id, @RequestBody Users newUser) {
        return repository.findById(id).map(user -> {
            if(newUser.getUsername() != null && !newUser.getUsername().isEmpty())
                user.setUsername(newUser.getUsername());
            if(newUser.getEmail() != null && !newUser.getEmail().isEmpty())
                user.setEmail(newUser.getEmail());
            return repository.save(user);
        }).orElseGet(() -> {
            return repository.save(newUser);
        });
    }
    // Add a favorite
    @CrossOrigin
    @PutMapping("/users/{id}/addFavorite/{fav_id}")
    Item addFavorite(@PathVariable Long id, @PathVariable Long fav_id){
        Item fav = itemRepository.findById(fav_id).orElse(null);
        repository.findById(id).map(user -> {
            if(fav == null || user.getFavorites().contains(fav))
                return user;
            user.addFavorite(fav);
            repository.save(user);
            return fav;
        });
        return fav;
    }
    // remove a favorite
    @CrossOrigin
    @PutMapping("/users/{id}/removeFavorite/{fav_id}")
    void removeFavorite(@PathVariable Long id, @PathVariable Long fav_id){
        repository.findById(id).map(user -> {
            Item fav = itemRepository.findById(fav_id).orElse(null);
           user.removeFavorite(fav);
           return repository.save(user);
        });
    }
    // Delete a user
    @CrossOrigin
    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
