package com.FoodSwiper;

import com.FoodSwiper.Entities.Groups;
import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.GroupRepository;
import com.FoodSwiper.Repositories.ItemRepository;
import com.FoodSwiper.Repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UsersController
 * @author Gael Romero
 */


class UsersControllerTest {

    UsersRepository usersRepository;
    ItemRepository itemRepository;
    GroupRepository groupRepository;
    UsersController controller;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        itemRepository = mock(ItemRepository.class);
        groupRepository = mock(GroupRepository.class);
        controller = new UsersController(usersRepository, itemRepository, groupRepository);
    }

    @Test
    void getAll_returnsAllUsers() {
        when(usersRepository.findAll()).thenReturn(List.of(
                new Users("Alice", "alice@test.com"),
                new Users("Bob", "bob@test.com")
        ));
        assertEquals(2, controller.all().size());
    }

    @Test
    void getAll_empty_returnsEmptyList() {
        when(usersRepository.findAll()).thenReturn(List.of());
        assertTrue(controller.all().isEmpty());
    }

    @Test
    void getUser_existingId_returnsUser() {
        Users u = new Users("Alice", "alice@test.com");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(u));
        assertEquals("Alice", controller.getUser(1L).getUsername());
    }

    @Test
    void getUser_notFound_returnsNull() {
        when(usersRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(controller.getUser(99L));
    }

    @Test
    void newUser_savesAndReturns() {
        Users u = new Users("Alice", "alice@test.com");
        when(usersRepository.save(u)).thenReturn(u);
        assertEquals("Alice", controller.newUser(u).getUsername());
        verify(usersRepository, times(1)).save(u);
    }

    @Test
    void editUser_existingId_updatesUsername() {
        Users existing = new Users("OldName", "old@test.com");
        Users updated = new Users("NewName", "");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(usersRepository.save(existing)).thenReturn(existing);

        controller.editUser(1L, updated);

        assertEquals("NewName", existing.getUsername());
    }

    @Test
    void editUser_existingId_updatesEmail() {
        Users existing = new Users("Alice", "old@test.com");
        Users updated = new Users("", "new@test.com");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(usersRepository.save(existing)).thenReturn(existing);

        controller.editUser(1L, updated);

        assertEquals("new@test.com", existing.getEmail());
    }

    @Test
    void editUser_emptyFields_doesNotOverwrite() {
        Users existing = new Users("Alice", "alice@test.com");
        Users updated = new Users("", "");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(usersRepository.save(existing)).thenReturn(existing);

        controller.editUser(1L, updated);

        assertEquals("Alice", existing.getUsername());
        assertEquals("alice@test.com", existing.getEmail());
    }

    @Test
    void editUser_notFound_createsNew() {
        Users u = new Users("Alice", "alice@test.com");
        when(usersRepository.findById(99L)).thenReturn(Optional.empty());
        when(usersRepository.save(u)).thenReturn(u);

        assertNotNull(controller.editUser(99L, u));
        verify(usersRepository).save(u);
    }

    @Test
    void addFavorite_validUserAndItem_returnsFavorite() {
        Users user = new Users("Alice", "alice@test.com");
        Item item = new Item("Pizza", "Italian");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        Item result = controller.addFavorite(1L, 2L);

        assertEquals(item, result);
    }

    @Test
    void addFavorite_itemNotFound_returnsNull() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(controller.addFavorite(1L, 99L));
    }

    @Test
    void addFavorite_alreadyFavorited_doesNotDuplicate() {
        Item item = new Item("Pizza", "Italian");
        Users user = new Users("Alice", "alice@test.com");
        user.addFavorite(item);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        controller.addFavorite(1L, 2L);

        assertEquals(1, user.getFavorites().size());
    }

    @Test
    void deleteUser_existingUser_deletesUser() {
        Users user = new Users("Alice", "alice@test.com");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        controller.deleteUser(1L);

        verify(usersRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_notFound_doesNothing() {
        when(usersRepository.findById(99L)).thenReturn(Optional.empty());

        controller.deleteUser(99L);

        verify(usersRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteUser_removesUserFromGroups() {
        Users user = new Users("Alice", "alice@test.com");
        user.addGroup(10L);
        Groups group = new Groups("TestGroup");
        group.addMember(user);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(groupRepository.findById(10L)).thenReturn(Optional.of(group));
        when(groupRepository.save(any())).thenReturn(group);

        controller.deleteUser(1L);

        verify(groupRepository).findById(10L);
        verify(usersRepository).deleteById(1L);
    }
}