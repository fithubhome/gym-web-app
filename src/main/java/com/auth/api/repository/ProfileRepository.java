package com.auth.api.repository;

import com.auth.api.model.Profile;
import com.auth.api.model.User;
import com.auth.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Repository
public class ProfileRepository {
    private final UserService userService;
    private final List<Profile> profiles = new ArrayList<>();

    @Autowired
    public ProfileRepository(UserService userService) {
        this.userService = userService;
        initializeProfiles();
    }

    private void initializeProfiles() {
        List<User> users = userService.getAllUsers();
        users.forEach(user -> {
            UUID userId = user.getId();
            profiles.add(new Profile(UUID.randomUUID(), userId));
        });
    }

    public Profile findByUserId(UUID userId) {
        return profiles.stream()
                .filter(profile -> profile.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void update(Profile profile) {
        IntStream.range(0, profiles.size())
                .filter(i -> profiles.get(i).getUserId().equals(profile.getUserId()))
                .findFirst()
                .ifPresent(i -> profiles.set(i, profile));
    }

    public void create(Profile profile) {
        boolean exists = profiles.stream()
                .anyMatch(prof -> prof.getId().equals(profile.getId()));
        if (!exists) {
            profiles.add(profile);
        }
    }
}
