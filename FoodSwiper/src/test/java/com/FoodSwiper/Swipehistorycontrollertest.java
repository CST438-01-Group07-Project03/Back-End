package com.FoodSwiper;

import com.FoodSwiper.Controllers.SwipeHistoryController;
import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.SwipeHistory;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.ItemRepository;
import com.FoodSwiper.Repositories.UsersRepository;
import com.FoodSwiper.Services.SwipeHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SwipeHistoryController
 * @author Gael Romero
 */
@ExtendWith(MockitoExtension.class)
class SwipeHistoryControllerTest {

    @Mock
    private SwipeHistoryService swipeHistoryService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private SwipeHistoryController controller;

    @Test
    void getLikes_returnsOkWithLikes() {
        SwipeHistory swipe = new SwipeHistory();
        swipe.setLiked(true);
        when(swipeHistoryService.getLikedItems(1L)).thenReturn(List.of(swipe));

        ResponseEntity<List<SwipeHistory>> response = controller.getLikes(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }



    @Test
    void getLikes_noLikes_returnsEmptyList() {
        when(swipeHistoryService.getLikedItems(1L)).thenReturn(List.of());

        ResponseEntity<List<SwipeHistory>> response = controller.getLikes(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void recordSwipe_validUserAndItem_returnsOk() {
        Users user = new Users("Alice", "alice@test.com");
        Item item = new Item("Pizza", "Italian");
        SwipeHistory swipe = new SwipeHistory();
        swipe.setUser(user);
        swipe.setItem(item);
        swipe.setLiked(true);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        when(swipeHistoryService.recordSwipe(any(), any(), anyBoolean())).thenReturn(swipe);

        ResponseEntity<?> response = controller.recordSwipe(1L, Map.of("itemId", 2, "liked", true));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void recordSwipe_userNotFound_returns404() {
        when(usersRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.recordSwipe(99L, Map.of("itemId", 2, "liked", true));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void recordSwipe_itemNotFound_returns404() {
        Users user = new Users("Alice", "alice@test.com");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.recordSwipe(1L, Map.of("itemId", 99, "liked", true));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void recordSwipe_pass_returnsOk() {
        Users user = new Users("Alice", "alice@test.com");
        Item item = new Item("Pizza", "Italian");
        SwipeHistory swipe = new SwipeHistory();
        swipe.setLiked(false);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        when(swipeHistoryService.recordSwipe(any(), any(), eq(false))).thenReturn(swipe);

        ResponseEntity<?> response = controller.recordSwipe(1L, Map.of("itemId", 2, "liked", false));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void unlike_existingSwipe_returns204() {
        when(swipeHistoryService.unlike(1L)).thenReturn(true);

        ResponseEntity<?> response = controller.unlike(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void unlike_notFound_returns404() {
        when(swipeHistoryService.unlike(99L)).thenReturn(false);

        ResponseEntity<?> response = controller.unlike(1L, 99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getFeed_returnsUnswipedItems() {
        Item i1 = new Item("Pizza", "Italian");
        Item i2 = new Item("Sushi", "Japanese");
        when(itemRepository.findAll()).thenReturn(List.of(i1, i2));
        when(swipeHistoryService.getUnswipedItems(anyLong(), any())).thenReturn(List.of(i2));

        ResponseEntity<List<Item>> response = controller.getFeed(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}