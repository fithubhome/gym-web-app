package com.gym_app.api.service;

import com.gym_app.api.model.Profile;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserEntity userEntity;

    public void createProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public void updateProfile(Profile updatedProfile) {
        Profile existingProfile = profileRepository.findById(updatedProfile.getId()).orElse(null);
        if (existingProfile != null) {
            logger.info("Updating profile for userEntity: {}", updatedProfile.getFirstName());
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
            logger.error("Profile not found for userEntity: {}", updatedProfile.getUserId());
        }
    }

    public Profile findProfileByUserId(UUID userId) {
        return profileRepository.findProfileByUserId(userId);
    }
}
