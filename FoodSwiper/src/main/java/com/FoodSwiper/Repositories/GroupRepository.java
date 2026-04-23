package com.FoodSwiper.Repositories;

import com.FoodSwiper.Entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> getLL();
}
