package com.FoodSwiper.Repositories;

import com.FoodSwiper.Entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/29/2026
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.type = ?1")
    List<Item> findByType(String type);
}
