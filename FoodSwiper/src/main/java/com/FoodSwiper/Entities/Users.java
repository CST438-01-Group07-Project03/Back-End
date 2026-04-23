package com.FoodSwiper.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@Entity
public class Users {
    @Id
    @GeneratedValue
    private long id;

    private String username;
    private String email;

    // List of groups
    @OneToMany
    List<Group> groups;
    // List of favorite foods/restaurants
    @OneToMany
    List<Item> favorites;
}
