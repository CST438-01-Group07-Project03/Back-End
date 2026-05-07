package com.FoodSwiper;

import com.FoodSwiper.Entities.Groups;
import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.Photo;
import com.FoodSwiper.Entities.SwipeHistory;
import com.FoodSwiper.Entities.Users;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all entities
 * @author Gael Romero
 */
class EntitiesTest {

    // --- Users ---

    @Test
    void users_constructor_setsFields() {
        Users u = new Users("Alice", "alice@test.com");
        assertEquals("Alice", u.getUsername());
        assertEquals("alice@test.com", u.getEmail());
        assertNotNull(u.getFavorites());
        assertNotNull(u.getGroup_ids());
        assertTrue(u.getFavorites().isEmpty());
        assertTrue(u.getGroup_ids().isEmpty());
    }

    @Test
    void users_defaultConstructor_createsEmpty() {
        Users u = new Users();
        assertNotNull(u);
        assertEquals("", u.getUsername());
        assertEquals("", u.getEmail());
    }

    @Test
    void users_setters_updateFields() {
        Users u = new Users();
        u.setUsername("Bob");
        u.setEmail("bob@test.com");
        assertEquals("Bob", u.getUsername());
        assertEquals("bob@test.com", u.getEmail());
    }

    @Test
    void users_addFavorite_addsFavorite() {
        Users u = new Users("Alice", "alice@test.com");
        Item item = new Item("Pizza", "Italian");
        u.addFavorite(item);
        assertEquals(1, u.getFavorites().size());
        assertTrue(u.getFavorites().contains(item));
    }

    @Test
    void users_removeFavorite_removesFavorite() {
        Users u = new Users("Alice", "alice@test.com");
        Item item = new Item("Pizza", "Italian");
        u.addFavorite(item);
        u.removeFavorite(item);
        assertTrue(u.getFavorites().isEmpty());
    }

    @Test
    void users_addGroup_addsGroupId() {
        Users u = new Users("Alice", "alice@test.com");
        u.addGroup(5L);
        assertTrue(u.getGroup_ids().contains(5L));
    }

    @Test
    void users_removeGroup_removesGroupId() {
        Users u = new Users("Alice", "alice@test.com");
        u.addGroup(5L);
        u.removeGroup(5L);
        assertFalse(u.getGroup_ids().contains(5L));
    }

    @Test
    void users_equals_sameFields_returnsTrue() {
        Users u1 = new Users("Alice", "alice@test.com");
        Users u2 = new Users("Alice", "alice@test.com");
        assertEquals(u1, u2);
    }

    @Test
    void users_equals_differentEmail_returnsFalse() {
        Users u1 = new Users("Alice", "alice@test.com");
        Users u2 = new Users("Alice", "other@test.com");
        assertNotEquals(u1, u2);
    }

    @Test
    void users_toString_containsUsername() {
        Users u = new Users("Alice", "alice@test.com");
        assertTrue(u.toString().contains("Alice"));
    }

    // --- Groups ---

    @Test
    void groups_constructor_setsNameAndEmptyMembers() {
        Groups g = new Groups("Friday Crew");
        assertEquals("Friday Crew", g.getName());
        assertNotNull(g.getMembers());
        assertTrue(g.getMembers().isEmpty());
    }

    @Test
    void groups_defaultConstructor_createsEmpty() {
        Groups g = new Groups();
        assertNotNull(g);
        assertEquals("", g.getName());
    }

    @Test
    void groups_addMember_addsMember() {
        Groups g = new Groups("Friday Crew");
        Users u = new Users("Alice", "alice@test.com");
        g.addMember(u);
        assertEquals(1, g.getMembers().size());
        assertTrue(g.getMembers().contains(u));
    }

    @Test
    void groups_removeMember_removesMember() {
        Groups g = new Groups("Friday Crew");
        Users u = new Users("Alice", "alice@test.com");
        g.addMember(u);
        g.removeMember(u);
        assertTrue(g.getMembers().isEmpty());
    }

    @Test
    void groups_setName_updatesName() {
        Groups g = new Groups("Old Name");
        g.setName("New Name");
        assertEquals("New Name", g.getName());
    }

    @Test
    void groups_toString_containsName() {
        Groups g = new Groups("Friday Crew");
        assertTrue(g.toString().contains("Friday Crew"));
    }

    // --- Item ---

    @Test
    void item_constructor_setsFields() {
        Item item = new Item("Pizza", "Italian food");
        assertEquals("Pizza", item.getName());
        assertEquals("Italian food", item.getDescription());
    }

    @Test
    void item_defaultConstructor_createsEmpty() {
        Item item = new Item();
        assertNotNull(item);
        assertEquals("", item.getName());
        assertEquals("", item.getDescription());
    }

    @Test
    void item_setters_updateFields() {
        Item item = new Item();
        item.setName("Sushi");
        item.setDescription("Japanese");
        item.setType("food");
        item.setYelpId("yelp123");
        item.setImageUrl("http://img.url");

        assertEquals("Sushi", item.getName());
        assertEquals("Japanese", item.getDescription());
        assertEquals("food", item.getType());
        assertEquals("yelp123", item.getYelpId());
        assertEquals("http://img.url", item.getImageUrl());
    }

    @Test
    void item_equals_sameFields_returnsTrue() {
        Item i1 = new Item("Pizza", "Italian");
        Item i2 = new Item("Pizza", "Italian");
        assertEquals(i1, i2);
    }

    @Test
    void item_equals_differentName_returnsFalse() {
        Item i1 = new Item("Pizza", "Italian");
        Item i2 = new Item("Sushi", "Japanese");
        assertNotEquals(i1, i2);
    }

    @Test
    void item_toString_containsName() {
        Item item = new Item("Pizza", "Italian");
        assertTrue(item.toString().contains("Pizza"));
    }

    // --- Photo ---

    @Test
    void photo_constructor_setsFields() {
        Item item = new Item("Pizza", "Italian");
        Photo photo = new Photo(item, "http://photo.url");
        assertEquals(item, photo.getItem());
        assertEquals("http://photo.url", photo.getUrl());
    }

    @Test
    void photo_defaultConstructor_createsEmpty() {
        Photo photo = new Photo();
        assertNotNull(photo);
        assertNull(photo.getItem());
        assertNull(photo.getUrl());
    }

    @Test
    void photo_setters_updateFields() {
        Photo photo = new Photo();
        Item item = new Item("Pizza", "Italian");
        photo.setItem(item);
        photo.setUrl("http://new.url");
        assertEquals(item, photo.getItem());
        assertEquals("http://new.url", photo.getUrl());
    }

    // --- SwipeHistory ---

    @Test
    void swipeHistory_setters_updateFields() {
        SwipeHistory swipe = new SwipeHistory();
        Users user = new Users("Alice", "alice@test.com");
        Item item = new Item("Pizza", "Italian");

        swipe.setUser(user);
        swipe.setItem(item);
        swipe.setLiked(true);

        assertEquals(user, swipe.getUser());
        assertEquals(item, swipe.getItem());
        assertTrue(swipe.isLiked());
    }

    @Test
    void swipeHistory_liked_false_setsCorrectly() {
        SwipeHistory swipe = new SwipeHistory();
        swipe.setLiked(false);
        assertFalse(swipe.isLiked());
    }
}