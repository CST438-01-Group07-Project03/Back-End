package com.FoodSwiper;

import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.UsersRepository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {

    private final UsersRepository usersRepository;

    public UserProfileController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @CrossOrigin
    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> attrs = principal.getAttributes();

        String email = (String) attrs.get("email");
        String name  = attrs.containsKey("name")  ? (String) attrs.get("name")
                      : attrs.containsKey("login") ? (String) attrs.get("login")
                      : email;

        Users dbUser;
        if (email != null) {
            dbUser = usersRepository.findByEmail(email)
                .orElseGet(() -> usersRepository.save(new Users(name, email)));
        } else {
            String sub = String.valueOf(attrs.get("id") != null ? attrs.get("id") : attrs.get("sub"));
            String syntheticEmail = "github-" + sub + "@noreply.github.com";
            dbUser = usersRepository.findByEmail(syntheticEmail)
                .orElseGet(() -> usersRepository.save(new Users(name, syntheticEmail)));
        }

        Map<String, Object> response = new HashMap<>(attrs);
        response.put("id", dbUser.getId());
        return response;
    }
}