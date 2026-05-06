package com.FoodSwiper;

import com.FoodSwiper.Entities.Item;
import com.FoodSwiper.Entities.Photo;
import com.FoodSwiper.Repositories.ItemRepository;
import com.FoodSwiper.Repositories.PhotoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@Profile("photo-upload")
public class YelpPhotoUploader {
    private static final Logger log = LoggerFactory.getLogger(YelpPhotoUploader.class);
    private static final String PHOTOS_JSON = "C:/Users/831Cr/Downloads/Yelp-Photos/Yelp Photos/photos.json";
    private static final String PHOTOS_DIR = "C:/Users/831Cr/Downloads/Yelp-Photos/Yelp Photos/photos/";
    private static final int MAX_PHOTOS = 3;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Value("${GOOGLE_APPLICATION_CREDENTIALS:#{null}}")
    private String credentialsPath;

    @Bean
    CommandLineRunner uploadPhotos(ItemRepository itemRepository, PhotoRepository photoRepository) {
        return args -> {
            Map<String, Item> businessMap = itemRepository.findAll().stream()
                    .filter(i -> i.getYelpId() != null && !i.getYelpId().isEmpty())
                    .collect(Collectors.toMap(Item::getYelpId, Function.identity(), (a, b) -> a));

            if (businessMap.isEmpty()) {
                log.warn("No items with yelpId found. Run the data loader first.");
                return;
            }
            log.info("Matching photos for {} restaurants...", businessMap.size());

            // Read photos.json — collect up to MAX_PHOTOS per business (food first, then outside)
            Map<String, List<String>> foodMap = new HashMap<>();
            Map<String, List<String>> outsideMap = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();

            try (BufferedReader br = new BufferedReader(new FileReader(PHOTOS_JSON))) {
                String line;
                while ((line = br.readLine()) != null) {
                    JsonNode node = mapper.readTree(line);
                    String businessId = node.path("business_id").asText("");
                    String photoId = node.path("photo_id").asText("");
                    String label = node.path("label").asText("");

                    if (!businessMap.containsKey(businessId) || photoId.isEmpty()) continue;

                    if ("food".equals(label)) {
                        List<String> list = foodMap.computeIfAbsent(businessId, k -> new ArrayList<>());
                        if (list.size() < MAX_PHOTOS) list.add(photoId);
                    } else if ("outside".equals(label)) {
                        List<String> list = outsideMap.computeIfAbsent(businessId, k -> new ArrayList<>());
                        if (list.size() < MAX_PHOTOS) list.add(photoId);
                    }
                }
            }

            // Merge: fill up to MAX_PHOTOS with food first, then outside
            Map<String, List<String>> businessToPhotos = new HashMap<>();
            for (String businessId : businessMap.keySet()) {
                List<String> photos = new ArrayList<>(foodMap.getOrDefault(businessId, List.of()));
                List<String> outside = outsideMap.getOrDefault(businessId, List.of());
                int remaining = MAX_PHOTOS - photos.size();
                if (remaining > 0) photos.addAll(outside.subList(0, Math.min(outside.size(), remaining)));
                if (!photos.isEmpty()) businessToPhotos.put(businessId, photos);
            }

            log.info("Found photos for {}/{} restaurants. Deleting the rest...",
                    businessToPhotos.size(), businessMap.size());

            // Delete restaurants with no photos
            int deleted = 0;
            for (Map.Entry<String, Item> entry : businessMap.entrySet()) {
                if (!businessToPhotos.containsKey(entry.getKey())) {
                    photoRepository.deleteAll(photoRepository.findByItem(entry.getValue()));
                    itemRepository.delete(entry.getValue());
                    deleted++;
                }
            }
            log.info("Deleted {} restaurants with no photos.", deleted);

            // Upload photos and save records
            Storage storage = buildStorage();
            int uploadedItems = 0;

            for (Map.Entry<String, List<String>> entry : businessToPhotos.entrySet()) {
                Item item = businessMap.get(entry.getKey());

                // Skip if already has photos in the Photo table
                if (!photoRepository.findByItem(item).isEmpty()) continue;

                List<Photo> photos = new ArrayList<>();
                boolean first = true;

                for (String photoId : entry.getValue()) {
                    java.nio.file.Path photoPath = Paths.get(PHOTOS_DIR + photoId + ".jpg");
                    if (!Files.exists(photoPath)) continue;

                    try {
                        BlobId blobId = BlobId.of(bucketName, photoId + ".jpg");
                        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
                        storage.create(blobInfo, Files.readAllBytes(photoPath));

                        String url = "https://storage.googleapis.com/" + bucketName + "/" + photoId + ".jpg";
                        photos.add(new Photo(item, url));

                        if (first) {
                            item.setImageUrl(url);
                            first = false;
                        }
                    } catch (Exception e) {
                        log.error("Failed to upload {}: {}", photoId, e.getMessage());
                    }
                }

                if (!photos.isEmpty()) {
                    photoRepository.saveAll(photos);
                    itemRepository.save(item);
                    uploadedItems++;

                    if (uploadedItems % 50 == 0) log.info("Processed {} restaurants...", uploadedItems);
                }
            }

            log.info("Done! Uploaded photos for {} restaurants.", uploadedItems);
        };
    }

    private Storage buildStorage() throws Exception {
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            return StorageOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(credentialsPath)))
                    .build()
                    .getService();
        }
        return StorageOptions.getDefaultInstance().getService();
    }
}