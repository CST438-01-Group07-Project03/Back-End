package com.FoodSwiper;

import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Services.CurrentUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class UserProfileController {

    
    private final CurrentUserService currentUserService;

    public UserProfileController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @CrossOrigin
    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User principal) {
        
        Users user = currentUserService.getOrCreateCurrentUser(principal);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("isAdmin", user.isAdmin());
        response.put("provider", user.getProvider());
        response.put("providerId", user.getProviderId());
        response.put("avatarUrl", user.getAvatarUrl());

        return response;
    }
}