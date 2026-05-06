package com.FoodSwiper;

import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Repositories.ItemRepository;
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
    CommandLineRunner loadRestaurants(ItemRepository itemRepository) {
        return args -> {
            if (itemRepository.count() > 0) {
                log.info("Restaurants already loaded, skipping.");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Item> batch = new ArrayList<>();
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

                    String name = node.path("name").asText("");

                    if (categories.length() > 500) {
                        categories = categories.substring(0, 500);
                    }

                    Item item = new Item(name, categories);
                    item.setType("restaurant");
                    item.setYelpId(node.path("business_id").asText(""));
                    batch.add(item);

                    if (batch.size() == 100) {
                        itemRepository.saveAll(batch);
                        count += batch.size();
                        batch.clear();
                        log.info("Loaded {} restaurants...", count);
                    }
                }

                if (!batch.isEmpty()) {
                    itemRepository.saveAll(batch);
                    count += batch.size();
                }
            }

            log.info("Done! Loaded {} Santa Barbara restaurants.", count);
        };
    }
}