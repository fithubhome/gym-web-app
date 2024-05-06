package com.auth.api.service;

import com.auth.api.model.Profile;
import com.auth.api.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public Profile getProfileByUserId(UUID userId) {
        return profileRepository.findByUserId(userId);
    }

    public void updateProfile(Profile profile) {
        profileRepository.update(profile);
    }

    public void createProfile(Profile profile) {
        profileRepository.create(profile);
    }
}