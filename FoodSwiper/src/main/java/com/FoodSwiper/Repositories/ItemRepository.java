package com.FoodSwiper.Repositories;

import com.FoodSwiper.Entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Peter Gloag
 * @since 4/29/2026
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
}
