package com.planora.planora.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trips", indexes = {
    @Index(name = "idx_trip_owner", columnList = "owner_id"),
    @Index(name = "idx_trip_dates", columnList = "start_date, end_date"),
    @Index(name = "idx_trip_status", columnList = "status")
})
public class Trip {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "Trip title is required")
    @Size(max = 200, message = "Trip title should not exceed 200 characters")
    private String title;
    
    @Column(nullable = false)
    @NotBlank(message = "Destination is required")
    @Size(max = 200, message = "Destination should not exceed 200 characters")
    private String destination;
    
    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status = TripStatus.PLANNING;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull(message = "Trip owner is required")
    private User owner;
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("date ASC")
    private List<DayItinerary> itinerary = new ArrayList<>();
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<TripCollaborator> collaborators = new HashSet<>();
    
    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private TripBudget budget;
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<TripDocument> documents = new HashSet<>();
    
    // Constructors
    public Trip() {}
    
    public Trip(String title, String destination, LocalDate startDate, LocalDate endDate, User owner) {
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner = owner;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TripStatus getStatus() {
        return status;
    }
    
    public void setStatus(TripStatus status) {
        this.status = status;
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
    
    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public List<DayItinerary> getItinerary() {
        return itinerary;
    }
    
    public void setItinerary(List<DayItinerary> itinerary) {
        this.itinerary = itinerary;
    }
    
    public Set<TripCollaborator> getCollaborators() {
        return collaborators;
    }
    
    public void setCollaborators(Set<TripCollaborator> collaborators) {
        this.collaborators = collaborators;
    }
    
    public TripBudget getBudget() {
        return budget;
    }
    
    public void setBudget(TripBudget budget) {
        this.budget = budget;
    }
    
    public Set<TripDocument> getDocuments() {
        return documents;
    }
    
    public void setDocuments(Set<TripDocument> documents) {
        this.documents = documents;
    }
    
    // Utility methods
    public void addDayItinerary(DayItinerary dayItinerary) {
        itinerary.add(dayItinerary);
        dayItinerary.setTrip(this);
    }
    
    public void removeDayItinerary(DayItinerary dayItinerary) {
        itinerary.remove(dayItinerary);
        dayItinerary.setTrip(null);
    }
    
    public void addCollaborator(TripCollaborator collaborator) {
        collaborators.add(collaborator);
        collaborator.setTrip(this);
    }
    
    public void removeCollaborator(TripCollaborator collaborator) {
        collaborators.remove(collaborator);
        collaborator.setTrip(null);
    }
    
    public void addDocument(TripDocument document) {
        documents.add(document);
        document.setTrip(this);
    }
    
    public void removeDocument(TripDocument document) {
        documents.remove(document);
        document.setTrip(null);
    }
    
    public boolean isOwner(User user) {
        return owner != null && owner.equals(user);
    }
    
    public boolean isCollaborator(User user) {
        return collaborators.stream()
                .anyMatch(collab -> collab.getUser().equals(user));
    }
    
    public boolean hasAccess(User user) {
        return isOwner(user) || isCollaborator(user);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;
        Trip trip = (Trip) o;
        return id != null && id.equals(trip.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", destination='" + destination + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}