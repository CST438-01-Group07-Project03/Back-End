package com.FoodSwiper.Controllers;

import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.ItemRepository;
import com.FoodSwiper.Repositories.PhotoRepository;
import com.FoodSwiper.Repositories.SwipeHistoryRepository;
import com.FoodSwiper.Services.CurrentUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    private final ItemRepository itemRepository;
    private final SwipeHistoryRepository swipeHistoryRepository;
    private final PhotoRepository photoRepository;
    private final CurrentUserService currentUserService;

    public AdminController(ItemRepository itemRepository,
                           SwipeHistoryRepository swipeHistoryRepository,
                           PhotoRepository photoRepository,
                           CurrentUserService currentUserService) {
        this.itemRepository = itemRepository;
        this.swipeHistoryRepository = swipeHistoryRepository;
        this.photoRepository = photoRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/items")
    public List<Item> getAllItems(@AuthenticationPrincipal OAuth2User principal) {
        requireAdmin(principal);
        return itemRepository.findAll();
    }

    @PostMapping("/items")
    public Item createItem(@AuthenticationPrincipal OAuth2User principal, @RequestBody Item newItem) {
        requireAdmin(principal);
        return itemRepository.save(newItem);
    }

    @PutMapping("/items/{id}")
    public Item updateItem(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable Long id,
            @RequestBody Item updatedItem
    ) {
        requireAdmin(principal);

        return itemRepository.findById(id).map(item -> {

            item.setName(updatedItem.getName());
            item.setDescription(updatedItem.getDescription());
            item.setType(updatedItem.getType());
            item.setYelpId(updatedItem.getYelpId());
            item.setImageUrl(updatedItem.getImageUrl());
            return itemRepository.save(item);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    @DeleteMapping("/items/{id}")
    @Transactional
    public void deleteItem(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        requireAdmin(principal);
        itemRepository.findById(id).ifPresent(item -> {
            swipeHistoryRepository.deleteAll(swipeHistoryRepository.findByItem(item));
            photoRepository.deleteAll(photoRepository.findByItem(item));
            itemRepository.delete(item);
        });
    }

    private void requireAdmin(OAuth2User principal) {
        Users user = currentUserService.getOrCreateCurrentUser(principal);
        if (!user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }
}