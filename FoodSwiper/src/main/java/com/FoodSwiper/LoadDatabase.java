package com.FoodSwiper;

import com.FoodSwiper.Entities.Groups;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.GroupRepository;
import com.FoodSwiper.Repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UsersRepository usersRepository, GroupRepository groupRepository){
        return args -> {
            log.info("Preloading" + usersRepository.save(new Users("TestUser", "TestUser@place.com")));
            log.info("Preloading" + groupRepository.save(new Groups("TestGroup")));
        };
    }
}
