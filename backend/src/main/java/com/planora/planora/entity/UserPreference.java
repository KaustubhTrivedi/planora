package com.planora.planora.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "preference_key"}),
       indexes = {
           @Index(name = "idx_user_preference_user", columnList = "user_id"),
           @Index(name = "idx_user_preference_key", columnList = "preference_key")
       })
public class UserPreference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "preference_key", nullable = false)
    @NotBlank(message = "Preference key is required")
    @Size(max = 100, message = "Preference key should not exceed 100 characters")
    private String preferenceKey;
    
    @Column(name = "preference_value", columnDefinition = "TEXT")
    private String preferenceValue;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Preference type is required")
    private PreferenceType type = PreferenceType.STRING;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;
    
    // Constructors
    public UserPreference() {}
    
    public UserPreference(String preferenceKey, String preferenceValue, PreferenceType type, User user) {
        this.preferenceKey = preferenceKey;
        this.preferenceValue = preferenceValue;
        this.type = type;
        this.user = user;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPreferenceKey() {
        return preferenceKey;
    }
    
    public void setPreferenceKey(String preferenceKey) {
        this.preferenceKey = preferenceKey;
    }
    
    public String getPreferenceValue() {
        return preferenceValue;
    }
    
    public void setPreferenceValue(String preferenceValue) {
        this.preferenceValue = preferenceValue;
    }
    
    public PreferenceType getType() {
        return type;
    }
    
    public void setType(PreferenceType type) {
        this.type = type;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    // Utility methods
    public Boolean getBooleanValue() {
        if (type == PreferenceType.BOOLEAN && preferenceValue != null) {
            return Boolean.parseBoolean(preferenceValue);
        }
        return null;
    }
    
    public Integer getIntegerValue() {
        if (type == PreferenceType.INTEGER && preferenceValue != null) {
            try {
                return Integer.parseInt(preferenceValue);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    public Double getDoubleValue() {
        if (type == PreferenceType.DECIMAL && preferenceValue != null) {
            try {
                return Double.parseDouble(preferenceValue);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPreference)) return false;
        UserPreference that = (UserPreference) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "UserPreference{" +
                "id=" + id +
                ", preferenceKey='" + preferenceKey + '\'' +
                ", preferenceValue='" + preferenceValue + '\'' +
                ", type=" + type +
                ", createdAt=" + createdAt +
                '}';
    }
}