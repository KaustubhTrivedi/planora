package com.planora.planora.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_collaborators", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"trip_id", "user_id"}),
       indexes = {
           @Index(name = "idx_trip_collaborator_trip", columnList = "trip_id"),
           @Index(name = "idx_trip_collaborator_user", columnList = "user_id"),
           @Index(name = "idx_trip_collaborator_role", columnList = "role")
       })
public class TripCollaborator {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Collaborator role is required")
    private CollaboratorRole role = CollaboratorRole.VIEWER;
    
    @Column(name = "invited_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime invitedAt;
    
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status = InvitationStatus.PENDING;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @NotNull(message = "Trip is required")
    private Trip trip;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_id")
    private User invitedBy;
    
    // Constructors
    public TripCollaborator() {}
    
    public TripCollaborator(Trip trip, User user, CollaboratorRole role, User invitedBy) {
        this.trip = trip;
        this.user = user;
        this.role = role;
        this.invitedBy = invitedBy;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public CollaboratorRole getRole() {
        return role;
    }
    
    public void setRole(CollaboratorRole role) {
        this.role = role;
    }
    
    public LocalDateTime getInvitedAt() {
        return invitedAt;
    }
    
    public void setInvitedAt(LocalDateTime invitedAt) {
        this.invitedAt = invitedAt;
    }
    
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
    
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public InvitationStatus getStatus() {
        return status;
    }
    
    public void setStatus(InvitationStatus status) {
        this.status = status;
    }
    
    public Trip getTrip() {
        return trip;
    }
    
    public void setTrip(Trip trip) {
        this.trip = trip;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public User getInvitedBy() {
        return invitedBy;
    }
    
    public void setInvitedBy(User invitedBy) {
        this.invitedBy = invitedBy;
    }
    
    // Utility methods
    public boolean canEdit() {
        return role == CollaboratorRole.EDITOR || role == CollaboratorRole.ADMIN;
    }
    
    public boolean canInvite() {
        return role == CollaboratorRole.ADMIN;
    }
    
    public boolean isActive() {
        return status == InvitationStatus.ACCEPTED;
    }
    
    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
        this.joinedAt = LocalDateTime.now();
    }
    
    public void decline() {
        this.status = InvitationStatus.DECLINED;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripCollaborator)) return false;
        TripCollaborator that = (TripCollaborator) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "TripCollaborator{" +
                "id=" + id +
                ", role=" + role +
                ", status=" + status +
                ", invitedAt=" + invitedAt +
                ", joinedAt=" + joinedAt +
                '}';
    }
}