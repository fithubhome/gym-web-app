package com.gym_app.api.repository;

import com.gym_app.api.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Profile findProfileByUserId(UUID userId);
}
