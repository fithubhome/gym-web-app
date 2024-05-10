package com.auth.api.service;

import com.auth.api.model.Profile;
import com.auth.api.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    private ProfileRepository profileRepository;

    public void createProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public void updateProfile(Profile updatedProfile) {
        Profile existingProfile = profileRepository.findById(updatedProfile.getId()).orElse(null);
        if (existingProfile != null) {
            logger.info("Updating profile for user: {}", updatedProfile.getUserId());
            existingProfile.setFirstName(updatedProfile.getFirstName());
            existingProfile.setLastName(updatedProfile.getLastName());
            existingProfile.setGender(updatedProfile.getGender());
            existingProfile.setDob(updatedProfile.getDob());
            existingProfile.setAddress(updatedProfile.getAddress());
            existingProfile.setPhone(updatedProfile.getPhone());
            if (updatedProfile.getImageData() != null && updatedProfile.getImageData().length > 0) {
                existingProfile.setImageData(updatedProfile.getImageData());
            }
            profileRepository.save(existingProfile);
        } else {
            logger.error("Profile not found for user: {}", updatedProfile.getUserId());
        }
    }

    public Profile getProfileById(UUID id) {
        return profileRepository.findById(id).orElse(null);
    }

    public Profile getProfileByUserId(UUID userId) {
        List<Profile> profiles = profileRepository.findAllByUserId(userId);
        if (profiles.size() > 1) {
            logger.error("Multiple profiles found for user: {}", userId);
        }
        return profiles.isEmpty() ? null : profiles.get(0);
    }
}
