package com.FoodSwiper;

import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ItemController
 * @author Gael Romero
 */

class ItemControllerTest {

    ItemRepository itemRepository;
    ItemController controller;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        controller = new ItemController(itemRepository);
    }

    @Test
    void getAll_returnsAllItems() {
        when(itemRepository.findAll()).thenReturn(List.of(
                new Item("Pizza", "Italian"),
                new Item("Sushi", "Japanese")
        ));
        assertEquals(2, controller.getAll().size());
    }

    @Test
    void getAll_empty_returnsEmptyList() {
        when(itemRepository.findAll()).thenReturn(List.of());
        assertTrue(controller.getAll().isEmpty());
    }

    @Test
    void getItem_existingId_returnsItem() {
        Item item = new Item("Pizza", "Italian");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertEquals("Pizza", controller.getItem(1L).getName());
    }

    @Test
    void getItem_notFound_returnsNull() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(controller.getItem(99L));
    }

    @Test
    void getItemsByType_returnsMatchingItems() {
        Item i1 = new Item("Pizza", "Italian");
        i1.setType("restaurant");
        when(itemRepository.findByType("restaurant")).thenReturn(List.of(i1));

        List<Item> result = controller.getItemsByType("restaurant");

        assertEquals(1, result.size());
        assertEquals("restaurant", result.get(0).getType());
    }

    @Test
    void getItemsByType_noMatches_returnsEmpty() {
        when(itemRepository.findByType("nonexistent")).thenReturn(List.of());
        assertTrue(controller.getItemsByType("nonexistent").isEmpty());
    }

    @Test
    void newItem_savesAndReturns() {
        Item item = new Item("Pizza", "Italian");
        when(itemRepository.save(item)).thenReturn(item);

        assertEquals("Pizza", controller.newItem(item).getName());
        verify(itemRepository).save(item);
    }

    @Test
    void editItem_existingId_savesItem() {
        Item existing = new Item("Pizza", "Italian");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(itemRepository.save(existing)).thenReturn(existing);

        assertNotNull(controller.editItem(1L, new Item("Updated", "New desc")));
        verify(itemRepository).save(existing);
    }

    @Test
    void editItem_notFound_createsNew() {
        Item newItem = new Item("Pizza", "Italian");
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());
        when(itemRepository.save(newItem)).thenReturn(newItem);

        assertNotNull(controller.editItem(99L, newItem));
        verify(itemRepository).save(newItem);
    }

    @Test
    void deleteItem_callsRepository() {
        controller.deleteItem(1L);
        verify(itemRepository).deleteById(1L);
    }
}