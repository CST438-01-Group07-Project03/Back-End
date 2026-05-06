package com.FoodSwiper.Entities;

import jakarta.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private String url;

    public Photo() {}

    public Photo(Item item, String url) {
        this.item = item;
        this.url = url;
    }

    public long getId() { return id; }
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}