package com.FoodSwiper.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.Objects;

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

    public Users(String username, String email){
        this.username = username;
        this.email = email;
    }
    public Users(){
        this("", "");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Item> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Item> favorites) {
        this.favorites = favorites;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return id == users.id && Objects.equals(username, users.username) && Objects.equals(email, users.email) && Objects.equals(groups, users.groups) && Objects.equals(favorites, users.favorites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, groups, favorites);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
