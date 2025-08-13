package com.planora.planora.service;

import com.planora.planora.dto.auth.*;
import com.planora.planora.entity.User;
import com.planora.planora.exception.ResourceNotFoundException;
import com.planora.planora.exception.ValidationException;
import com.planora.planora.repository.UserRepository;
import com.planora.planora.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtTokenService jwtTokenService;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";
    private final String encodedPassword = "encodedPassword123";
    private final String accessToken = "access.token.here";
    private final String refreshToken = "refresh.token.here";
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "jwtExpirationMs", 86400000L);
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(testEmail);
        testUser.setPasswordHash(encodedPassword);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmailVerified(true);
        testUser.setCreatedAt(LocalDateTime.now());
        
        registerRequest = new RegisterRequest();
        registerRequest.setEmail(testEmail);
        registerRequest.setPassword(testPassword);
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        
        loginRequest = new LoginRequest();
        loginRequest.setEmail(testEmail);
        loginRequest.setPassword(testPassword);
    }
    
    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Given
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        
        // When
        UserDetails userDetails = userService.loadUserByUsername(testEmail);
        
        // Then
        assertNotNull(userDetails);
        assertEquals(testEmail, userDetails.getUsername());
        assertTrue(userDetails instanceof UserPrincipal);
        verify(userRepository).findByEmail(testEmail);
    }
    
    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(testEmail);
        });
        verify(userRepository).findByEmail(testEmail);
    }
    
    @Test
    void registerUser_ShouldReturnAuthResponse_WhenValidRequest() {
        // Given
        when(userRepository.existsByEmail(testEmail)).thenReturn(false);
        when(passwordEncoder.encode(testPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtTokenService.generateTokenFromUsername(testEmail)).thenReturn(accessToken);
        when(jwtTokenService.generateRefreshToken(testEmail)).thenReturn(refreshToken);
        
        // When
        AuthResponse response = userService.registerUser(registerRequest);
        
        // Then
        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertNotNull(response.getUser());
        assertEquals(testEmail, response.getUser().getEmail());
        
        verify(userRepository).existsByEmail(testEmail);
        verify(passwordEncoder).encode(testPassword);
        verify(userRepository).save(any(User.class));
        verify(jwtTokenService).generateTokenFromUsername(testEmail);
        verify(jwtTokenService).generateRefreshToken(testEmail);
    }
    
    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(testEmail)).thenReturn(true);
        
        // When & Then
        assertThrows(ValidationException.class, () -> {
            userService.registerUser(registerRequest);
        });
        verify(userRepository).existsByEmail(testEmail);
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void authenticateUser_ShouldReturnAuthResponse_WhenValidCredentials() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtTokenService.generateToken(authentication)).thenReturn(accessToken);
        when(jwtTokenService.generateRefreshToken(testEmail)).thenReturn(refreshToken);
        
        // When
        AuthResponse response = userService.authenticateUser(loginRequest);
        
        // Then
        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertNotNull(response.getUser());
        assertEquals(testEmail, response.getUser().getEmail());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(testEmail);
        verify(userRepository).save(any(User.class));
        verify(jwtTokenService).generateToken(authentication);
        verify(jwtTokenService).generateRefreshToken(testEmail);
    }
    
    @Test
    void refreshToken_ShouldReturnAuthResponse_WhenValidRefreshToken() {
        // Given
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        when(jwtTokenService.validateToken(refreshToken)).thenReturn(true);
        when(jwtTokenService.isRefreshToken(refreshToken)).thenReturn(true);
        when(jwtTokenService.getUsernameFromToken(refreshToken)).thenReturn(testEmail);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(jwtTokenService.generateTokenFromUsername(testEmail)).thenReturn(accessToken);
        
        // When
        AuthResponse response = userService.refreshToken(request);
        
        // Then
        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertNotNull(response.getUser());
        assertEquals(testEmail, response.getUser().getEmail());
        
        verify(jwtTokenService).validateToken(refreshToken);
        verify(jwtTokenService).isRefreshToken(refreshToken);
        verify(jwtTokenService).getUsernameFromToken(refreshToken);
        verify(userRepository).findByEmail(testEmail);
        verify(jwtTokenService).generateTokenFromUsername(testEmail);
    }
    
    @Test
    void refreshToken_ShouldThrowException_WhenInvalidRefreshToken() {
        // Given
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        when(jwtTokenService.validateToken(refreshToken)).thenReturn(false);
        
        // When & Then
        assertThrows(ValidationException.class, () -> {
            userService.refreshToken(request);
        });
        verify(jwtTokenService).validateToken(refreshToken);
    }
    
    @Test
    void updateProfile_ShouldReturnUpdatedUser_WhenValidRequest() {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest("Jane", "Smith");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // When
        User updatedUser = userService.updateProfile(1L, request);
        
        // Then
        assertNotNull(updatedUser);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void updateProfile_ShouldThrowException_WhenUserNotFound() {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest("Jane", "Smith");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateProfile(1L, request);
        });
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        // When
        User user = userService.getUserById(1L);
        
        // Then
        assertNotNull(user);
        assertEquals(testUser.getId(), user.getId());
        assertEquals(testUser.getEmail(), user.getEmail());
        verify(userRepository).findById(1L);
    }
    
    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });
        verify(userRepository).findById(1L);
    }
    
    @Test
    void getCurrentUser_ShouldReturnUser_WhenAuthenticated() {
        // Given
        UserPrincipal userPrincipal = UserPrincipal.create(testUser);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        SecurityContextHolder.setContext(securityContext);
        
        // When
        User currentUser = userService.getCurrentUser();
        
        // Then
        assertNotNull(currentUser);
        assertEquals(testUser.getId(), currentUser.getId());
        verify(userRepository).findById(1L);
    }
    
    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        // Given
        when(userRepository.existsByEmail(testEmail)).thenReturn(true);
        
        // When
        boolean exists = userService.existsByEmail(testEmail);
        
        // Then
        assertTrue(exists);
        verify(userRepository).existsByEmail(testEmail);
    }
    
    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        // Given
        when(userRepository.existsByEmail(testEmail)).thenReturn(false);
        
        // When
        boolean exists = userService.existsByEmail(testEmail);
        
        // Then
        assertFalse(exists);
        verify(userRepository).existsByEmail(testEmail);
    }
    
    @Test
    void verifyEmail_ShouldUpdateEmailVerified_WhenUserExists() {
        // Given
        testUser.setEmailVerified(false);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // When
        userService.verifyEmail(testEmail);
        
        // Then
        verify(userRepository).findByEmail(testEmail);
        verify(userRepository).save(any(User.class));
    }
}