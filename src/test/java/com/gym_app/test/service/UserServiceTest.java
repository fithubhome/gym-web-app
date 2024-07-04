package com.gym_app.test.service;

import com.gym_app.api.exceptions.auth.DuplicateUserException;
import com.gym_app.api.model.Profile;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.repository.UserRepository;
import com.gym_app.api.service.ProfileService;
import com.gym_app.api.service.RoleService;
import com.gym_app.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProfileService profileService;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<UserEntity> users = List.of(new UserEntity(), new UserEntity());
        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = userService.getAllUsers();
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserById() {
        UUID userId = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserEntity result = userService.findUserById(userId);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserEntity result = userService.findByEmail(email);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testRegisterNewUserSuccess() throws DuplicateUserException {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setId(UUID.randomUUID());

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        userService.registerNewUser(user);

        verify(userRepository, times(1)).save(user);
        verify(profileService, times(1)).createProfile(any(Profile.class));
        verify(roleService, times(1)).assignRoleToUser(user.getId(), "MEMBER");
    }

    @Test
    public void testRegisterNewUserAdmin() throws DuplicateUserException {
        String email = "admin@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setId(UUID.randomUUID());

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        userService.registerNewUser(user);

        verify(userRepository, times(1)).save(user);
        verify(profileService, times(1)).createProfile(any(Profile.class));
        verify(roleService, times(1)).assignRoleToUser(user.getId(), "ADMIN");
    }

    @Test
    public void testRegisterNewUserDuplicateEmail() {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(DuplicateUserException.class, () -> userService.registerNewUser(user));

        verify(userRepository, times(0)).save(user);
        verify(profileService, times(0)).createProfile(any(Profile.class));
        verify(roleService, times(0)).assignRoleToUser(any(UUID.class), anyString());
    }

    @Test
    public void testRegisterNewUserInvalidEmail() {
        UserEntity user = new UserEntity();
        user.setEmail("");

        assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(user));

        verify(userRepository, times(0)).save(user);
        verify(profileService, times(0)).createProfile(any(Profile.class));
        verify(roleService, times(0)).assignRoleToUser(any(UUID.class), anyString());
    }

    @Test
    public void testFindById() {
        UUID userId = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userService.findById(userId);
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }
}
