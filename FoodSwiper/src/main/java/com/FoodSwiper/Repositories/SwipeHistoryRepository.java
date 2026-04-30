package com.FoodSwiper.Repositories;

import com.FoodSwiper.Entities.SwipeHistory;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SwipeHistoryRepository extends JpaRepository<SwipeHistory, Long> {

    List<SwipeHistory> findByUser(Users user);

    List<SwipeHistory> findByUserAndLikedTrue(Users user);

    List<SwipeHistory> findByUserAndLikedFalse(Users user);

    Optional<SwipeHistory> findByUserAndItem(Users user, Item item);

    List<SwipeHistory> findByUser_Id(long userId);

    List<SwipeHistory> findByUser_IdAndLikedTrue(long userId);
}