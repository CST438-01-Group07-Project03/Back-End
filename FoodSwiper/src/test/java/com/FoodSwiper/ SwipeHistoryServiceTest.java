package com.FoodSwiper;

import com.FoodSwiper.Entities.Food;
import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.Restaurant;
import com.FoodSwiper.Entities.SwipeHistory;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.SwipeHistoryRepository;
import com.FoodSwiper.Services.SwipeHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SwipeHistoryServiceTest {

    @Mock
    private SwipeHistoryRepository swipeHistoryRepository;

    @InjectMocks
    private SwipeHistoryService swipeHistoryService;

    private Users testUser;
    private Food testFood;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testFood = new Food();
        testRestaurant = new Restaurant();
    }

    @Test
    void recordSwipe_newLike_savesRecord() {
        when(swipeHistoryRepository.findByUserAndItem(testUser, testFood))
                .thenReturn(Optional.empty());

        SwipeHistory saved = new SwipeHistory();
        saved.setUser(testUser);
        saved.setItem(testFood);
        saved.setLiked(true);
        when(swipeHistoryRepository.save(any(SwipeHistory.class))).thenReturn(saved);

        SwipeHistory result = swipeHistoryService.recordSwipe(testUser, testFood, true);

        assertTrue(result.isLiked());
        verify(swipeHistoryRepository, times(1)).save(any(SwipeHistory.class));
    }

    @Test
    void recordSwipe_pass_savesWithLikedFalse() {
        when(swipeHistoryRepository.findByUserAndItem(testUser, testFood))
                .thenReturn(Optional.empty());

        SwipeHistory saved = new SwipeHistory();
        saved.setLiked(false);
        when(swipeHistoryRepository.save(any(SwipeHistory.class))).thenReturn(saved);

        SwipeHistory result = swipeHistoryService.recordSwipe(testUser, testFood, false);

        assertFalse(result.isLiked());
    }

    @Test
    void recordSwipe_existingRecord_updatesInsteadOfCreatingNew() {
        SwipeHistory existing = new SwipeHistory();
        existing.setUser(testUser);
        existing.setItem(testFood);
        existing.setLiked(false);

        when(swipeHistoryRepository.findByUserAndItem(testUser, testFood))
                .thenReturn(Optional.of(existing));
        when(swipeHistoryRepository.save(existing)).thenReturn(existing);

        swipeHistoryService.recordSwipe(testUser, testFood, true);

        verify(swipeHistoryRepository, times(1)).save(existing);
    }

    @Test
    void getLikedItems_returnsOnlyLikedRecords() {
        SwipeHistory like = new SwipeHistory();
        like.setLiked(true);

        when(swipeHistoryRepository.findByUser_IdAndLikedTrue(1L))
                .thenReturn(List.of(like));

        List<SwipeHistory> result = swipeHistoryService.getLikedItems(1L);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isLiked());
    }

    @Test
    void unlike_existingRecord_deletesAndReturnsTrue() {
        when(swipeHistoryRepository.existsById(1L)).thenReturn(true);

        boolean result = swipeHistoryService.unlike(1L);

        assertTrue(result);
        verify(swipeHistoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void unlike_nonExistentRecord_returnsFalse() {
        when(swipeHistoryRepository.existsById(99L)).thenReturn(false);

        boolean result = swipeHistoryService.unlike(99L);

        assertFalse(result);
        verify(swipeHistoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void getUnswipedItems_excludesAlreadySwipedItems() {
        SwipeHistory pastSwipe = new SwipeHistory();
        pastSwipe.setItem(testFood);

        when(swipeHistoryRepository.findByUser_Id(1L))
                .thenReturn(List.of(pastSwipe));

        List<Item> allItems = List.of(testFood, testRestaurant);

        List<Item> result = swipeHistoryService.getUnswipedItems(1L, allItems);

        assertEquals(1, result.size());
        assertFalse(result.contains(testFood));
    }
}