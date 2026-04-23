package com.FoodSwiper.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@Entity
public class Item {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String description;
}
