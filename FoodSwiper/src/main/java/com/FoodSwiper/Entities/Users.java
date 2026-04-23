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

    @OneToMany
    List<Group> groups;

    @OneToMany
    List<Item> favorites;

    public long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
