package com.FoodSwiper.Services;

import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.UsersRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CurrentUserService {

    private final UsersRepository usersRepository;

    public CurrentUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users getOrCreateCurrentUser(OAuth2User principal) {
        Map<String, Object> attrs = principal.getAttributes();

        String provider = detectProvider(attrs);
        String providerId = firstNonBlank(
                valueAsString(attrs.get("id")),
                valueAsString(attrs.get("sub"))
        );

        String email = valueAsString(attrs.get("email"));
        if ((email == null || email.isBlank()) && "github".equals(provider) && providerId != null) {
            // fallback for GitHub private email
            email = "github-" + providerId + "@noreply.github.com";
        }

        String username = firstNonBlank(
                valueAsString(attrs.get("name")),
                valueAsString(attrs.get("login")),
                email,
                "User"
        );

        String avatarUrl = firstNonBlank(
                valueAsString(attrs.get("picture")),
                valueAsString(attrs.get("avatar_url"))
        );

        Optional<Users> existingUser = Optional.empty();

        if (providerId != null && !providerId.isBlank()) {
            existingUser = usersRepository.findByProviderAndProviderId(provider, providerId);
        }

        if (existingUser.isEmpty() && email != null && !email.isBlank()) {
            existingUser = usersRepository.findByEmail(email);
        }

        Users user = existingUser.orElse(null);
        if (user == null) {
            user = new Users(username, email);
        }

        // CHANGED: sync app user fields
        user.setUsername(username);
        user.setEmail(email);
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setAvatarUrl(avatarUrl);

        return usersRepository.save(user);
    }

    public boolean isAdmin(OAuth2User principal) {
        Users user = getOrCreateCurrentUser(principal);
        return user.isAdmin();
    }

    private String detectProvider(Map<String, Object> attrs) {
        if (attrs.containsKey("avatar_url") || attrs.containsKey("login")) {
            return "github";
        }
        return "google";
    }

    private String valueAsString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}