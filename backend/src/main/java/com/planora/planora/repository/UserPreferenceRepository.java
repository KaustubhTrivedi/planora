package com.planora.planora.repository;

import com.planora.planora.entity.PreferenceType;
import com.planora.planora.entity.User;
import com.planora.planora.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    
    /**
     * Find preferences by user
     */
    List<UserPreference> findByUser(User user);
    
    /**
     * Find preference by user and key
     */
    Optional<UserPreference> findByUserAndPreferenceKey(User user, String preferenceKey);
    
    /**
     * Find preferences by user and type
     */
    List<UserPreference> findByUserAndType(User user, PreferenceType type);
    
    /**
     * Find preferences by key
     */
    List<UserPreference> findByPreferenceKey(String preferenceKey);
    
    /**
     * Find preferences by type
     */
    List<UserPreference> findByType(PreferenceType type);
    
    /**
     * Find preferences by key pattern (case-insensitive)
     */
    @Query("SELECT up FROM UserPreference up WHERE LOWER(up.preferenceKey) LIKE LOWER(CONCAT('%', :keyPattern, '%'))")
    List<UserPreference> findByPreferenceKeyContainingIgnoreCase(@Param("keyPattern") String keyPattern);
    
    /**
     * Find preferences by value pattern (case-insensitive)
     */
    @Query("SELECT up FROM UserPreference up WHERE LOWER(up.preferenceValue) LIKE LOWER(CONCAT('%', :valuePattern, '%'))")
    List<UserPreference> findByPreferenceValueContainingIgnoreCase(@Param("valuePattern") String valuePattern);
    
    /**
     * Check if preference exists for user and key
     */
    boolean existsByUserAndPreferenceKey(User user, String preferenceKey);
    
    /**
     * Count preferences by user
     */
    long countByUser(User user);
    
    /**
     * Count preferences by type
     */
    long countByType(PreferenceType type);
    
    /**
     * Find users with a specific preference key
     */
    @Query("SELECT DISTINCT up.user FROM UserPreference up WHERE up.preferenceKey = :preferenceKey")
    List<User> findUsersByPreferenceKey(@Param("preferenceKey") String preferenceKey);
    
    /**
     * Find users with a specific preference key and value
     */
    @Query("SELECT DISTINCT up.user FROM UserPreference up WHERE up.preferenceKey = :preferenceKey " +
           "AND up.preferenceValue = :preferenceValue")
    List<User> findUsersByPreferenceKeyAndValue(@Param("preferenceKey") String preferenceKey,
                                               @Param("preferenceValue") String preferenceValue);
    
    /**
     * Delete preference by user and key
     */
    void deleteByUserAndPreferenceKey(User user, String preferenceKey);
    
    /**
     * Delete all preferences for a user
     */
    void deleteByUser(User user);
    
    /**
     * Find boolean preferences by user
     */
    @Query("SELECT up FROM UserPreference up WHERE up.user = :user AND up.type = 'BOOLEAN'")
    List<UserPreference> findBooleanPreferencesByUser(@Param("user") User user);
    
    /**
     * Find string preferences by user
     */
    @Query("SELECT up FROM UserPreference up WHERE up.user = :user AND up.type = 'STRING'")
    List<UserPreference> findStringPreferencesByUser(@Param("user") User user);
    
    /**
     * Find numeric preferences by user (INTEGER and DECIMAL)
     */
    @Query("SELECT up FROM UserPreference up WHERE up.user = :user " +
           "AND (up.type = 'INTEGER' OR up.type = 'DECIMAL')")
    List<UserPreference> findNumericPreferencesByUser(@Param("user") User user);
    
    /**
     * Find JSON preferences by user
     */
    @Query("SELECT up FROM UserPreference up WHERE up.user = :user AND up.type = 'JSON'")
    List<UserPreference> findJsonPreferencesByUser(@Param("user") User user);
}