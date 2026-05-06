package com.FoodSwiper.Repositories;

import com.FoodSwiper.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}