package com.planora.planora.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planora.planora.dto.auth.*;
import com.planora.planora.entity.User;
import com.planora.planora.exception.ValidationException;
import com.planora.planora.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private AuthController authController;
    
    private ObjectMapper objectMapper;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    private User testUser;
    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        
        registerRequest = new RegisterRequest();
        registerRequest.setEmail(testEmail);
        registerRequest.setPassword(testPassword);
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        
        loginRequest = new LoginRequest();
        loginRequest.setEmail(testEmail);
        loginRequest.setPassword(testPassword);
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            1L, testEmail, "John", "Doe", true
        );
        
        authResponse = new AuthResponse(
            "access.token.here",
            "refresh.token.here",
            86400L,
            userInfo
        );
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(testEmail);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmailVerified(true);
        testUser.setCreatedAt(LocalDateTime.now());
    }
    
    @Test
    void registerUser_ShouldReturnAuthResponse_WhenValidRequest() throws Exception {
        // Given
        when(userService.registerUser(any(RegisterRequest.class))).thenReturn(authResponse);
        
        // When & Then
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access.token.here"))
                .andExpect(jsonPath("$.refresh_token").value("refresh.token.here"))
                .andExpect(jsonPath("$.user.email").value(testEmail))
                .andExpect(jsonPath("$.user.firstName").value("John"));
        
        verify(userService).registerUser(any(RegisterRequest.class));
    }
    
    @Test
    void registerUser_ShouldReturnBadRequest_WhenEmailAlreadyExists() throws Exception {
        // Given
        when(userService.registerUser(any(RegisterRequest.class)))
                .thenThrow(new ValidationException("Email is already registered"));
        
        // When & Then
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Registration failed"));
        
        verify(userService).registerUser(any(RegisterRequest.class));
    }
    
    @Test
    void authenticateUser_ShouldReturnAuthResponse_WhenValidCredentials() throws Exception {
        // Given
        when(userService.authenticateUser(any(LoginRequest.class))).thenReturn(authResponse);
        
        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access.token.here"))
                .andExpect(jsonPath("$.refresh_token").value("refresh.token.here"))
                .andExpect(jsonPath("$.user.email").value(testEmail));
        
        verify(userService).authenticateUser(any(LoginRequest.class));
    }
    
    @Test
    void authenticateUser_ShouldReturnUnauthorized_WhenInvalidCredentials() throws Exception {
        // Given
        when(userService.authenticateUser(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));
        
        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Authentication failed"));
        
        verify(userService).authenticateUser(any(LoginRequest.class));
    }
    
    @Test
    void refreshToken_ShouldReturnAuthResponse_WhenValidRefreshToken() throws Exception {
        // Given
        RefreshTokenRequest request = new RefreshTokenRequest("refresh.token.here");
        when(userService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(authResponse);
        
        // When & Then
        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access.token.here"))
                .andExpect(jsonPath("$.refresh_token").value("refresh.token.here"));
        
        verify(userService).refreshToken(any(RefreshTokenRequest.class));
    }
    
    @Test
    void refreshToken_ShouldReturnUnauthorized_WhenInvalidRefreshToken() throws Exception {
        // Given
        RefreshTokenRequest request = new RefreshTokenRequest("invalid.token");
        when(userService.refreshToken(any(RefreshTokenRequest.class)))
                .thenThrow(new ValidationException("Invalid refresh token"));
        
        // When & Then
        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Token refresh failed"));
        
        verify(userService).refreshToken(any(RefreshTokenRequest.class));
    }
    
    @Test
    void checkEmail_ShouldReturnEmailExists_WhenEmailExists() throws Exception {
        // Given
        when(userService.existsByEmail(testEmail)).thenReturn(true);
        
        // When & Then
        mockMvc.perform(get("/auth/check-email")
                .param("email", testEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testEmail))
                .andExpect(jsonPath("$.exists").value(true));
        
        verify(userService).existsByEmail(testEmail);
    }
    
    @Test
    void checkEmail_ShouldReturnEmailNotExists_WhenEmailDoesNotExist() throws Exception {
        // Given
        when(userService.existsByEmail(testEmail)).thenReturn(false);
        
        // When & Then
        mockMvc.perform(get("/auth/check-email")
                .param("email", testEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testEmail))
                .andExpect(jsonPath("$.exists").value(false));
        
        verify(userService).existsByEmail(testEmail);
    }
}