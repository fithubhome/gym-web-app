package com.gym_app.test.model;

import com.gym_app.api.model.Role;
import com.gym_app.api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
    }

    @Test
    public void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String email = "test@example.com";
        String password = "password";
        List<Role> roles = new ArrayList<>();

        userEntity.setId(id);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setRoles(roles);

        assertEquals(id, userEntity.getId());
        assertEquals(email, userEntity.getEmail());
        assertEquals(password, userEntity.getPassword());
        assertEquals(roles, userEntity.getRoles());
    }

    @Test
    public void testConstructorWithParameters() {
        String email = "test@example.com";
        String password = "password";
        UserEntity userEntity = new UserEntity(email, password);

        assertEquals(email, userEntity.getEmail());
        assertEquals(password, userEntity.getPassword());
    }

    @Test
    public void testPrePersist() {
        UserEntity userEntity = new UserEntity();
        userEntity.prePersist();
        assertNotNull(userEntity.getId());
    }
}
