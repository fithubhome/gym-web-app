package com.auth.api.repository;

import com.auth.api.model.Profile;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProfileRepository {
    private static final Map<Integer, Profile> profiles = new HashMap<>();

    static {
        initializeMockProfiles();
    }

    private static void initializeMockProfiles() {
        profiles.put(1, new Profile(1, 1, "Tim", "Smith", 'M', new Date(), "123 Apple St", "1234567890", "/images/tim.jpg"));
        profiles.put(2, new Profile(2, 2, "Mike", "Brown", 'M', new Date(), "234 Banana Ave", "345678901", "/images/mike.jpg"));
        profiles.put(3, new Profile(3, 3, "Paul", "Jones", 'M', new Date(), "345 Cherry Blvd", "456789012", "/images/paul.jpg"));
        profiles.put(4, new Profile(4, 4, "Cristina", "Lee", 'F', new Date(), "456 Date Dr", "567890123", "/images/cristina.jpg"));
        profiles.put(5, new Profile(5, 5, "Simida", "White", 'F', new Date(), "567 Fig Ct", "678901234", "/images/simida.jpg"));
        profiles.put(6, new Profile(6, 6, "Alin", "Black", 'M', new Date(), "678 Grape Ln", "789012345", "/images/alin.jpg"));
        profiles.put(7, new Profile(7, 7, "Flavi", "Grey", 'M', new Date(), "789 Kiwi Rd", "890123456", "/images/flavi.jpg"));
        profiles.put(8, new Profile(8, 8, "Andrei", "Green", 'M', new Date(), "890 Lemon St", "901234567", "/images/andrei.jpg"));
        profiles.put(9, new Profile(9, 9, "Mihai", "Gold", 'M', new Date(), "901 Lime Blvd", "12345678", "/images/mihai.jpg"));
        profiles.put(10, new Profile(10, 10, "Alina", "Silver", 'F', new Date(), "12 Melon Ln", "1012345678", "/images/alina.jpg"));
        profiles.put(11, new Profile(11, 11, "Darius", "Bronze", 'M', new Date(), "23 Nectarine Dr", "1123456789", "/images/darius.jpg"));
    }

    public Profile findByUserId(int userId) {
        return profiles.get(userId);
    }

    public void update(Profile profile) {
        profiles.put(profile.getUserId(), profile);
    }

    public void create(Profile profile) {
        if (profiles.containsKey(profile.getUserId())) {
            // If profile already exists, return false
            return;
        }
        // Add new profile to the map
        profiles.put(profile.getUserId(), profile);
    }
}
