package com.FoodSwiper;

import com.FoodSwiper.Entities.Restaurant;
import com.FoodSwiper.Repositories.RestaurantRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class YelpDataLoader {
    private static final Logger log = LoggerFactory.getLogger(YelpDataLoader.class);
    private static final String YELP_FILE = "C:/Users/831Cr/Downloads/Yelp-JSON/Yelp JSON/yelp_dataset/yelp_academic_dataset_business.json";

    @Bean
    CommandLineRunner loadRestaurants(RestaurantRepository restaurantRepository) {
        return args -> {
            if (restaurantRepository.count() > 0) {
                log.info("Restaurants already loaded, skipping.");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Restaurant> batch = new ArrayList<>();
            int count = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(YELP_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    JsonNode node = mapper.readTree(line);

                    String city = node.path("city").asText("");
                    String state = node.path("state").asText("");
                    int isOpen = node.path("is_open").asInt(0);
                    String categories = node.path("categories").asText("");

                    if (!"Santa Barbara".equals(city) || !"CA".equals(state)
                            || isOpen != 1 || !categories.contains("Restaurants")) {
                        continue;
                    }

                    String yelpId = node.path("business_id").asText("");
                    String name = node.path("name").asText("");
                    String address = node.path("address").asText("");
                    String zipCode = node.path("postal_code").asText("");
                    double stars = node.path("stars").asDouble(0);
                    int reviewCount = node.path("review_count").asInt(0);

                    if (categories.length() > 500) {
                        categories = categories.substring(0, 500);
                    }

                    batch.add(new Restaurant(name, yelpId, address, city, state, zipCode, stars, reviewCount, categories, ""));

                    if (batch.size() == 100) {
                        restaurantRepository.saveAll(batch);
                        count += batch.size();
                        batch.clear();
                        log.info("Loaded {} restaurants...", count);
                    }
                }

                if (!batch.isEmpty()) {
                    restaurantRepository.saveAll(batch);
                    count += batch.size();
                }
            }

            log.info("Done! Loaded {} Santa Barbara restaurants.", count);
        };
    }
}