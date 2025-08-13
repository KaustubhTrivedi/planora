package com.planora.planora.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "activities", indexes = {
    @Index(name = "idx_activity_day_itinerary", columnList = "day_itinerary_id"),
    @Index(name = "idx_activity_type", columnList = "type"),
    @Index(name = "idx_activity_start_time", columnList = "day_itinerary_id, start_time")
})
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "Activity name is required")
    @Size(max = 200, message = "Activity name should not exceed 200 characters")
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    @NotBlank(message = "Location is required")
    @Size(max = 300, message = "Location should not exceed 300 characters")
    private String location;
    
    @Column(name = "place_id")
    @Size(max = 100, message = "Place ID should not exceed 100 characters")
    private String placeId;
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "end_time")
    private LocalTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Activity type is required")
    private ActivityType type;
    
    @Column(name = "estimated_cost", precision = 10, scale = 2)
    @PositiveOrZero(message = "Estimated cost must be positive or zero")
    private BigDecimal estimatedCost;
    
    @Column(name = "actual_cost", precision = 10, scale = 2)
    @PositiveOrZero(message = "Actual cost must be positive or zero")
    private BigDecimal actualCost;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "booking_reference")
    @Size(max = 100, message = "Booking reference should not exceed 100 characters")
    private String bookingReference;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_itinerary_id", nullable = false)
    @NotNull(message = "Day itinerary is required")
    private DayItinerary dayItinerary;
    
    // Constructors
    public Activity() {}
    
    public Activity(String name, String location, ActivityType type, DayItinerary dayItinerary) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.dayItinerary = dayItinerary;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getPlaceId() {
        return placeId;
    }
    
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public ActivityType getType() {
        return type;
    }
    
    public void setType(ActivityType type) {
        this.type = type;
    }
    
    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }
    
    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
    
    public BigDecimal getActualCost() {
        return actualCost;
    }
    
    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getBookingReference() {
        return bookingReference;
    }
    
    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
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
    
    public DayItinerary getDayItinerary() {
        return dayItinerary;
    }
    
    public void setDayItinerary(DayItinerary dayItinerary) {
        this.dayItinerary = dayItinerary;
    }
    
    // Utility methods
    public boolean hasTimeConflict(Activity other) {
        if (this.startTime == null || this.endTime == null || 
            other.startTime == null || other.endTime == null) {
            return false;
        }
        
        return !(this.endTime.isBefore(other.startTime) || 
                 this.startTime.isAfter(other.endTime));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity activity = (Activity) o;
        return id != null && id.equals(activity.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", type=" + type +
                ", estimatedCost=" + estimatedCost +
                ", createdAt=" + createdAt +
                '}';
    }
}