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

    public void createProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public void updateProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public Profile getProfileById(UUID id) {
        return profileRepository.findById(id).orElse(null);
    }

}
