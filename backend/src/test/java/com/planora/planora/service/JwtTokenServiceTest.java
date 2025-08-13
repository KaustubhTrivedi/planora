package com.planora.planora.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {
    
    @InjectMocks
    private JwtTokenService jwtTokenService;
    
    private final String testSecret = "mySecretKey123456789012345678901234567890123456789012345678901234567890";
    private final long testExpiration = 86400000L; // 24 hours
    private final long testRefreshExpiration = 604800000L; // 7 days
    private final String testEmail = "test@example.com";
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenService, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtTokenService, "jwtExpirationMs", testExpiration);
        ReflectionTestUtils.setField(jwtTokenService, "jwtRefreshExpirationMs", testRefreshExpiration);
    }
    
    @Test
    void generateToken_ShouldReturnValidToken() {
        // Given
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(testEmail);
        
        // When
        String token = jwtTokenService.generateToken(authentication);
        
        // Then
        assertNotNull(token);
        assertTrue(jwtTokenService.validateToken(token));
        assertEquals(testEmail, jwtTokenService.getUsernameFromToken(token));
    }
    
    @Test
    void generateTokenFromUsername_ShouldReturnValidToken() {
        // When
        String token = jwtTokenService.generateTokenFromUsername(testEmail);
        
        // Then
        assertNotNull(token);
        assertTrue(jwtTokenService.validateToken(token));
        assertEquals(testEmail, jwtTokenService.getUsernameFromToken(token));
    }
    
    @Test
    void generateRefreshToken_ShouldReturnValidRefreshToken() {
        // When
        String refreshToken = jwtTokenService.generateRefreshToken(testEmail);
        
        // Then
        assertNotNull(refreshToken);
        assertTrue(jwtTokenService.validateToken(refreshToken));
        assertTrue(jwtTokenService.isRefreshToken(refreshToken));
        assertEquals(testEmail, jwtTokenService.getUsernameFromToken(refreshToken));
    }
    
    @Test
    void getUsernameFromToken_ShouldReturnCorrectUsername() {
        // Given
        String token = jwtTokenService.generateTokenFromUsername(testEmail);
        
        // When
        String extractedEmail = jwtTokenService.getUsernameFromToken(token);
        
        // Then
        assertEquals(testEmail, extractedEmail);
    }
    
    @Test
    void getExpirationDateFromToken_ShouldReturnCorrectDate() {
        // Given
        String token = jwtTokenService.generateTokenFromUsername(testEmail);
        
        // When
        Date expirationDate = jwtTokenService.getExpirationDateFromToken(token);
        
        // Then
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }
    
    @Test
    void isTokenExpired_ShouldReturnFalseForValidToken() {
        // Given
        String token = jwtTokenService.generateTokenFromUsername(testEmail);
        
        // When
        Boolean isExpired = jwtTokenService.isTokenExpired(token);
        
        // Then
        assertFalse(isExpired);
    }
    
    @Test
    void isTokenExpired_ShouldReturnTrueForExpiredToken() {
        // Given - Create an expired token by setting a very short expiration
        ReflectionTestUtils.setField(jwtTokenService, "jwtExpirationMs", 1L); // 1 millisecond
        String expiredToken = jwtTokenService.generateTokenFromUsername(testEmail);
        
        // Wait for token to expire
        try {
            Thread.sleep(10); // Wait 10ms to ensure token is expired
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Reset expiration for other tests
        ReflectionTestUtils.setField(jwtTokenService, "jwtExpirationMs", testExpiration);
        
        // When
        Boolean isExpired = jwtTokenService.isTokenExpired(expiredToken);
        
        // Then
        assertTrue(isExpired);
    }
    
    @Test
    void validateToken_WithUserDetails_ShouldReturnTrueForValidToken() {
        // Given
        String token = jwtTokenService.generateTokenFromUsername(testEmail);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(testEmail);
        
        // When
        Boolean isValid = jwtTokenService.validateToken(token, userDetails);
        
        // Then
        assertTrue(isValid);
    }
    
    @Test
    void validateToken_WithUserDetails_ShouldReturnFalseForDifferentUser() {
        // Given
        String token = jwtTokenService.generateTokenFromUsername(testEmail);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("different@example.com");
        
        // When
        Boolean isValid = jwtTokenService.validateToken(token, userDetails);
        
        // Then
        assertFalse(isValid);
    }
    
    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        // Given
        String token = jwtTokenService.generateTokenFromUsername(testEmail);
        
        // When
        Boolean isValid = jwtTokenService.validateToken(token);
        
        // Then
        assertTrue(isValid);
    }
    
    @Test
    void validateToken_ShouldReturnFalseForInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";
        
        // When
        Boolean isValid = jwtTokenService.validateToken(invalidToken);
        
        // Then
        assertFalse(isValid);
    }
    
    @Test
    void isRefreshToken_ShouldReturnTrueForRefreshToken() {
        // Given
        String refreshToken = jwtTokenService.generateRefreshToken(testEmail);
        
        // When
        Boolean isRefresh = jwtTokenService.isRefreshToken(refreshToken);
        
        // Then
        assertTrue(isRefresh);
    }
    
    @Test
    void isRefreshToken_ShouldReturnFalseForAccessToken() {
        // Given
        String accessToken = jwtTokenService.generateTokenFromUsername(testEmail);
        
        // When
        Boolean isRefresh = jwtTokenService.isRefreshToken(accessToken);
        
        // Then
        assertFalse(isRefresh);
    }
    
    @Test
    void getTokenExpirationTime_ShouldReturnPositiveValue() {
        // Given
        String token = jwtTokenService.generateTokenFromUsername(testEmail);
        
        // When
        long expirationTime = jwtTokenService.getTokenExpirationTime(token);
        
        // Then
        assertTrue(expirationTime > 0);
    }
    
    @Test
    void getTokenExpirationTime_ShouldReturnZeroForInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";
        
        // When
        long expirationTime = jwtTokenService.getTokenExpirationTime(invalidToken);
        
        // Then
        assertEquals(0, expirationTime);
    }
}