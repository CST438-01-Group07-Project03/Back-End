package com.FoodSwiper.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import org.apache.catalina.User;

import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@Entity
public class Group {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    @ManyToMany
    private List<Users> members;
    // List of foods/restaurants to be voted on
}
