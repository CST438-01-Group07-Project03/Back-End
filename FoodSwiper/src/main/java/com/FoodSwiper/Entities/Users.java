package com.FoodSwiper.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
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
    private List<Long> group_ids;
    @OneToMany
    private List<Item> favorites;

    public Users(String username, String email){
        this.username = username;
        this.email = email;
        this.group_ids = new ArrayList<>();
        this.favorites = new ArrayList<>();
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

    public List<Item> getFavorites(){
        return this.favorites;
    }
    public void setFavorites(List<Item> favorites){
        this.favorites = favorites;
    }
    public void addFavorite(Item new_item){
        this.favorites.add(new_item);
    }
    public void removeFavorite(Item to_remove){
        this.favorites.remove(to_remove);
    }

    public List<Long> getGroup_ids(){
        return this.group_ids;
    }
    public void setGroup_ids(List<Long> group_ids){
        this.group_ids = group_ids;
    }
    public void addGroup(Long new_group_id){
        this.group_ids.add(new_group_id);
    }
    public void removeGroup(Long to_remove){
        this.group_ids.remove(to_remove);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return id == users.id && Objects.equals(username, users.username) && Objects.equals(email, users.email);// && Objects.equals(group_ids, users.group_ids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);//, group_ids);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", group_ids=" + group_ids +
                ", favorites=" + favorites +
                '}';
    }
}
