package com.FoodSwiper.Controllers;

import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.SwipeHistory;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.ItemRepository;
import com.FoodSwiper.Repositories.UsersRepository;
import com.FoodSwiper.Services.SwipeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class SwipeHistoryController {

    @Autowired
    private SwipeHistoryService swipeHistoryService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/{id}/likes")
    public ResponseEntity<List<SwipeHistory>> getLikes(@PathVariable long id) {
        List<SwipeHistory> likes = swipeHistoryService.getLikedItems(id);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/{id}/likes/foods")
    public ResponseEntity<List<SwipeHistory>> getLikedFoods(@PathVariable long id) {
        List<SwipeHistory> likes = swipeHistoryService.getLikedFoods(id);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/{id}/likes/restaurants")
    public ResponseEntity<List<SwipeHistory>> getLikedRestaurants(@PathVariable long id) {
        List<SwipeHistory> likes = swipeHistoryService.getLikedRestaurants(id);
        return ResponseEntity.ok(likes);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<?> recordSwipe(@PathVariable long id,
                                         @RequestBody Map<String, Object> body) {
        Users user = usersRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Long itemId = Long.valueOf(body.get("itemId").toString());
        boolean liked = Boolean.parseBoolean(body.get("liked").toString());

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        SwipeHistory swipe = swipeHistoryService.recordSwipe(user, item, liked);
        return ResponseEntity.ok(swipe);
    }

    @DeleteMapping("/{id}/likes/{likeId}")
    public ResponseEntity<?> unlike(@PathVariable long id,
                                    @PathVariable long likeId) {
        boolean deleted = swipeHistoryService.unlike(likeId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}