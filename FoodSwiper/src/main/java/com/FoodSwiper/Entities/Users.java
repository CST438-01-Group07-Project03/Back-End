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

    // OAuth + admin support
    private boolean isAdmin;
    private String provider;
    private String providerId;
    private String avatarUrl;

    @ElementCollection // CHANGED: needed for List<Long>
    private List<Long> group_ids;

    @OneToMany
    private List<Item> favorites;

    public Users(String username, String email){
        this.username = username;
        this.email = email;

        // ADDED: default values for new users
        this.isAdmin = false;
        this.provider = "";
        this.providerId = "";
        this.avatarUrl = "";

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

    // ADDED
    public boolean isAdmin() {
        return isAdmin;
    }

    // ADDED
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    // ADDED
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    // ADDED
    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    // ADDED
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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
        return id == users.id
                && isAdmin == users.isAdmin // CHANGED
                && Objects.equals(username, users.username)
                && Objects.equals(email, users.email)
                && Objects.equals(provider, users.provider)
                && Objects.equals(providerId, users.providerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, isAdmin, provider, providerId);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", provider='" + provider + '\'' +
                ", providerId='" + providerId + '\'' +
                ", group_ids=" + group_ids +
                ", favorites=" + favorites +
                '}';
    }
}
