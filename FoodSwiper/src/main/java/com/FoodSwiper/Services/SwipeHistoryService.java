package com.FoodSwiper.Services;

import com.FoodSwiper.Entities.Food;
import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.Restaurant;
import com.FoodSwiper.Entities.SwipeHistory;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.SwipeHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SwipeHistoryService {

    @Autowired
    private SwipeHistoryRepository swipeHistoryRepository;

    public SwipeHistory recordSwipe(Users user, Item item, boolean liked) {
        Optional<SwipeHistory> existing = swipeHistoryRepository.findByUserAndItem(user, item);

        SwipeHistory swipe = existing.orElse(new SwipeHistory());
        swipe.setUser(user);
        swipe.setItem(item);
        swipe.setLiked(liked);

        return swipeHistoryRepository.save(swipe);
    }

    public List<SwipeHistory> getLikedItems(long userId) {
        return swipeHistoryRepository.findByUser_IdAndLikedTrue(userId);
    }


    public List<SwipeHistory> getLikedFoods(long userId) {
        return swipeHistoryRepository.findByUser_IdAndLikedTrue(userId)
                .stream()
                .filter(s -> s.getItem() instanceof Food)
                .collect(Collectors.toList());
    }

    public List<SwipeHistory> getLikedRestaurants(long userId) {
        return swipeHistoryRepository.findByUser_IdAndLikedTrue(userId)
                .stream()
                .filter(s -> s.getItem() instanceof Restaurant)
                .collect(Collectors.toList());
    }

    public boolean unlike(long swipeId) {
        if (swipeHistoryRepository.existsById(swipeId)) {
            swipeHistoryRepository.deleteById(swipeId);
            return true;
        }
        return false;
    }

    public List<Item> getUnswipedItems(long userId, List<Item> allItems) {
        List<Long> swipedIds = swipeHistoryRepository.findByUser_Id(userId)
                .stream()
                .map(s -> (Long) s.getItem().getId())
                .collect(Collectors.toList());

        List<Item> unswiped = allItems.stream()
                .filter(item -> !swipedIds.contains(item.getId()))
                .collect(Collectors.toList());

        Collections.shuffle(unswiped);
        return unswiped;
    }
}