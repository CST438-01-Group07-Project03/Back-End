package com.FoodSwiper.Repositories;

import com.FoodSwiper.Entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
