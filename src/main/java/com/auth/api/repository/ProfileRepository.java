package com.auth.api.repository;

import com.auth.api.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    @Query("SELECT p FROM Profile p WHERE p.userId = :userId")
    List<Profile> findAllByUserId(@Param("userId") UUID userId);
}
