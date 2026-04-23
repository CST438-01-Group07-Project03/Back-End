package com.FoodSwiper.Repositories;

import com.FoodSwiper.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
public interface UsersRepository extends JpaRepository<Users, Long> {
}
