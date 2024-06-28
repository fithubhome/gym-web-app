package com.gym_app.test.controller;

import com.gym_app.api.controller.AuthController;
import com.gym_app.api.dto.auth.LoginDTO;
import com.gym_app.api.dto.auth.RegisterDTO;
import com.gym_app.api.exceptions.auth.DuplicateUserException;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.repository.UserRepository;
import com.gym_app.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowLoginForm() {
        String viewName = authController.showLoginForm(model);
        assertEquals("auth/login", viewName);
        verify(model).addAttribute(eq("loginDto"), any(LoginDTO.class));
    }

    @Test
    public void testLoginSuccess() {
        LoginDTO loginDto = new LoginDTO();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        String viewName = authController.login(loginDto, model);
        assertEquals("redirect:/dashboard", viewName);
    }

    @Test
    public void testLoginFailure() {
        LoginDTO loginDto = new LoginDTO();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        when(userService.findByEmail(anyString())).thenReturn(null);

        String viewName = authController.login(loginDto, model);
        assertEquals("auth/login", viewName);
        verify(model).addAttribute(eq("error"), anyString());
    }

    @Test
    public void testShowRegistrationForm() {
        String viewName = authController.showRegistrationForm(model);
        assertEquals("auth/register", viewName);
        verify(model).addAttribute(eq("registerUser"), any(RegisterDTO.class));
    }

    @Test
    public void testRegisterSuccess() throws DuplicateUserException {
        RegisterDTO registerDto = new RegisterDTO();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        String viewName = authController.register(registerDto, model);
        assertEquals("redirect:/auth/login", viewName);
        verify(userService).registerNewUser(any(UserEntity.class));
    }

    @Test
    public void testRegisterFailureEmailExists() throws DuplicateUserException {
        RegisterDTO registerDto = new RegisterDTO();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        String viewName = authController.register(registerDto, model);
        assertEquals("auth/register", viewName);
        verify(model).addAttribute(eq("error"), anyString());
    }
}
