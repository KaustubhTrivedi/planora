package com.planora.planora.service;

import com.planora.planora.dto.auth.*;
import com.planora.planora.entity.User;
import com.planora.planora.exception.ResourceNotFoundException;
import com.planora.planora.exception.ValidationException;
import com.planora.planora.repository.UserRepository;
import com.planora.planora.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for user management and authentication operations
 */
@Service
@Transactional
public class UserService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenService jwtTokenService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Value("${spring.security.jwt.expiration}")
    private long jwtExpirationMs;
    
    /**
     * Load user by username (email) for Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return UserPrincipal.create(user);
    }
    
    /**
     * Register a new user
     */
    public AuthResponse registerUser(RegisterRequest request) {
        logger.info("Attempting to register user with email: {}", request.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email is already registered");
        }
        
        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailVerified(true); // For now, auto-verify emails
        
        User savedUser = userRepository.save(user);
        logger.info("Successfully registered user with ID: {}", savedUser.getId());
        
        // Generate tokens
        String accessToken = jwtTokenService.generateTokenFromUsername(savedUser.getEmail());
        String refreshToken = jwtTokenService.generateRefreshToken(savedUser.getEmail());
        
        // Create user info
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getFirstName(),
            savedUser.getLastName(),
            savedUser.getEmailVerified()
        );
        
        return new AuthResponse(accessToken, refreshToken, jwtExpirationMs / 1000, userInfo);
    }
    
    /**
     * Authenticate user and generate tokens
     */
    public AuthResponse authenticateUser(LoginRequest request) {
        logger.info("Attempting to authenticate user with email: {}", request.getEmail());
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Get user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Update last login time
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        // Generate tokens
        String accessToken = jwtTokenService.generateToken(authentication);
        String refreshToken = jwtTokenService.generateRefreshToken(user.getEmail());
        
        logger.info("Successfully authenticated user with ID: {}", user.getId());
        
        // Create user info
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmailVerified()
        );
        
        return new AuthResponse(accessToken, refreshToken, jwtExpirationMs / 1000, userInfo);
    }
    
    /**
     * Refresh access token using refresh token
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        // Validate refresh token
        if (!jwtTokenService.validateToken(refreshToken) || !jwtTokenService.isRefreshToken(refreshToken)) {
            throw new ValidationException("Invalid refresh token");
        }
        
        // Extract username from refresh token
        String email = jwtTokenService.getUsernameFromToken(refreshToken);
        
        // Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        // Generate new access token
        String newAccessToken = jwtTokenService.generateTokenFromUsername(email);
        
        logger.info("Successfully refreshed token for user: {}", email);
        
        // Create user info
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmailVerified()
        );
        
        return new AuthResponse(newAccessToken, refreshToken, jwtExpirationMs / 1000, userInfo);
    }
    
    /**
     * Update user profile
     */
    public User updateProfile(Long userId, UpdateProfileRequest request) {
        logger.info("Updating profile for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Update fields if provided
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        
        User updatedUser = userRepository.save(user);
        logger.info("Successfully updated profile for user ID: {}", userId);
        
        return updatedUser;
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
    
    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Get current authenticated user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("No authenticated user found");
        }
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return getUserById(userPrincipal.getId());
    }
    
    /**
     * Check if email exists
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Verify user email (placeholder for future email verification)
     */
    public void verifyEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        user.setEmailVerified(true);
        userRepository.save(user);
        
        logger.info("Email verified for user: {}", email);
    }
}