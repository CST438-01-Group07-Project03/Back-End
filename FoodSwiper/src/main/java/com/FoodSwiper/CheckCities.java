package com.FoodSwiper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class CheckCities {
    public static void main(String[] args) throws Exception {
        String path = "C:/Users/831Cr/Downloads/Yelp-JSON/Yelp JSON/yelp_dataset/yelp_academic_dataset_business.json";
        Map<String, Integer> cities = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                boolean isRestaurant = line.contains("Restaurants");
                boolean isOpen = line.contains("\"is_open\":1") || line.contains("\"is_open\": 1");
                if (isRestaurant && isOpen) {
                    String city = extractField(line, "city");
                    String state = extractField(line, "state");
                    if (city != null && state != null)
                        cities.merge(city + ", " + state, 1, Integer::sum);
                }
            }
        }

        cities.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(30)
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }

    private static String extractField(String json, String field) {
        String key = "\"" + field + "\":\"";
        int start = json.indexOf(key);
        if (start == -1) return null;
        start += key.length();
        int end = json.indexOf("\"", start);
        return end == -1 ? null : json.substring(start, end);
    }
}
