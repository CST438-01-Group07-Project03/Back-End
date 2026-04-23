package com.FoodSwiper.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
public interface FoodRepository extends JpaRepository<Food, Long> {
}
