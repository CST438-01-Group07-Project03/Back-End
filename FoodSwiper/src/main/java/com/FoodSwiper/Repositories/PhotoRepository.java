package com.FoodSwiper.Repositories;

import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByItem(Item item);
}