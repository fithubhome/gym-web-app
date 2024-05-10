package com.auth.api.repository;

import com.auth.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findByUserId(UUID userId);
    boolean existsByUserIdAndRoleType(UUID userId, String roleType);
    void deleteByUserIdAndRoleType(UUID userId, String roleType);
}
