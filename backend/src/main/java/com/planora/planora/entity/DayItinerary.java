package com.planora.planora.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "day_itineraries", indexes = {
    @Index(name = "idx_day_itinerary_trip_date", columnList = "trip_id, date", unique = true),
    @Index(name = "idx_day_itinerary_day_number", columnList = "trip_id, day_number")
})
public class DayItinerary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @Column(name = "day_number", nullable = false)
    @NotNull(message = "Day number is required")
    @Positive(message = "Day number must be positive")
    private Integer dayNumber;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @NotNull(message = "Trip is required")
    private Trip trip;
    
    @OneToMany(mappedBy = "dayItinerary", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("startTime ASC")
    private List<Activity> activities = new ArrayList<>();
    
    // Constructors
    public DayItinerary() {}
    
    public DayItinerary(LocalDate date, Integer dayNumber, Trip trip) {
        this.date = date;
        this.dayNumber = dayNumber;
        this.trip = trip;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Integer getDayNumber() {
        return dayNumber;
    }
    
    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    
    public Trip getTrip() {
        return trip;
    }
    
    public void setTrip(Trip trip) {
        this.trip = trip;
    }
    
    public List<Activity> getActivities() {
        return activities;
    }
    
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
    
    // Utility methods
    public void addActivity(Activity activity) {
        activities.add(activity);
        activity.setDayItinerary(this);
    }
    
    public void removeActivity(Activity activity) {
        activities.remove(activity);
        activity.setDayItinerary(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayItinerary)) return false;
        DayItinerary that = (DayItinerary) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "DayItinerary{" +
                "id=" + id +
                ", date=" + date +
                ", dayNumber=" + dayNumber +
                ", activitiesCount=" + (activities != null ? activities.size() : 0) +
                ", createdAt=" + createdAt +
                '}';
    }
}