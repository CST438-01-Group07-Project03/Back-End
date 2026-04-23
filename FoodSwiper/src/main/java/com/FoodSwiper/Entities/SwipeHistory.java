package com.FoodSwiper.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Gael Romero
 * @since 4/22/2026
 */
@Entity
public class SwipeHistory {

    @Id
    @GeneratedValue
    private long id;

    /** The user who performed the swipe */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    /** The item that was swiped on (Food or Restaurant — both extend Item) */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /** true = swiped right (liked), false = swiped left (passed) */
    private boolean liked;

    /** Timestamp of when the swipe occurred */
    private LocalDateTime swipedAt;

    @PrePersist
    protected void onCreate() {
        this.swipedAt = LocalDateTime.now();
    }

    public long getId() { return id; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public boolean isLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }

    public LocalDateTime getSwipedAt() { return swipedAt; }
}