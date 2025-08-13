package com.planora.planora.repository;

import com.planora.planora.entity.CollaboratorRole;
import com.planora.planora.entity.InvitationStatus;
import com.planora.planora.entity.Trip;
import com.planora.planora.entity.TripCollaborator;
import com.planora.planora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripCollaboratorRepository extends JpaRepository<TripCollaborator, Long> {
    
    /**
     * Find collaborators by trip
     */
    List<TripCollaborator> findByTrip(Trip trip);
    
    /**
     * Find collaborators by trip and status
     */
    List<TripCollaborator> findByTripAndStatus(Trip trip, InvitationStatus status);
    
    /**
     * Find active collaborators by trip
     */
    @Query("SELECT tc FROM TripCollaborator tc WHERE tc.trip = :trip AND tc.status = 'ACCEPTED'")
    List<TripCollaborator> findActiveCollaboratorsByTrip(@Param("trip") Trip trip);
    
    /**
     * Find collaborations by user
     */
    List<TripCollaborator> findByUser(User user);
    
    /**
     * Find active collaborations by user
     */
    @Query("SELECT tc FROM TripCollaborator tc WHERE tc.user = :user AND tc.status = 'ACCEPTED'")
    List<TripCollaborator> findActiveCollaborationsByUser(@Param("user") User user);
    
    /**
     * Find collaboration by trip and user
     */
    Optional<TripCollaborator> findByTripAndUser(Trip trip, User user);
    
    /**
     * Find collaborators by role
     */
    List<TripCollaborator> findByRole(CollaboratorRole role);
    
    /**
     * Find collaborators by trip and role
     */
    List<TripCollaborator> findByTripAndRole(Trip trip, CollaboratorRole role);
    
    /**
     * Find pending invitations by user
     */
    @Query("SELECT tc FROM TripCollaborator tc WHERE tc.user = :user AND tc.status = 'PENDING'")
    List<TripCollaborator> findPendingInvitationsByUser(@Param("user") User user);
    
    /**
     * Find pending invitations by trip
     */
    @Query("SELECT tc FROM TripCollaborator tc WHERE tc.trip = :trip AND tc.status = 'PENDING'")
    List<TripCollaborator> findPendingInvitationsByTrip(@Param("trip") Trip trip);
    
    /**
     * Find collaborations invited by a specific user
     */
    List<TripCollaborator> findByInvitedBy(User invitedBy);
    
    /**
     * Find expired invitations
     */
    @Query("SELECT tc FROM TripCollaborator tc WHERE tc.status = 'PENDING' " +
           "AND tc.invitedAt < :expirationDate")
    List<TripCollaborator> findExpiredInvitations(@Param("expirationDate") LocalDateTime expirationDate);
    
    /**
     * Check if user is collaborator on trip
     */
    boolean existsByTripAndUser(Trip trip, User user);
    
    /**
     * Check if user is active collaborator on trip
     */
    @Query("SELECT COUNT(tc) > 0 FROM TripCollaborator tc WHERE tc.trip = :trip " +
           "AND tc.user = :user AND tc.status = 'ACCEPTED'")
    boolean existsActiveCollaborationByTripAndUser(@Param("trip") Trip trip, @Param("user") User user);
    
    /**
     * Count collaborators by trip
     */
    long countByTrip(Trip trip);
    
    /**
     * Count active collaborators by trip
     */
    @Query("SELECT COUNT(tc) FROM TripCollaborator tc WHERE tc.trip = :trip AND tc.status = 'ACCEPTED'")
    long countActiveCollaboratorsByTrip(@Param("trip") Trip trip);
    
    /**
     * Count collaborations by user
     */
    long countByUser(User user);
    
    /**
     * Find collaborators with edit permissions
     */
    @Query("SELECT tc FROM TripCollaborator tc WHERE tc.trip = :trip " +
           "AND tc.status = 'ACCEPTED' " +
           "AND (tc.role = 'EDITOR' OR tc.role = 'ADMIN')")
    List<TripCollaborator> findEditorsAndAdminsByTrip(@Param("trip") Trip trip);
    
    /**
     * Find admin collaborators by trip
     */
    @Query("SELECT tc FROM TripCollaborator tc WHERE tc.trip = :trip " +
           "AND tc.status = 'ACCEPTED' AND tc.role = 'ADMIN'")
    List<TripCollaborator> findAdminsByTrip(@Param("trip") Trip trip);
    
    /**
     * Delete collaboration by trip and user
     */
    void deleteByTripAndUser(Trip trip, User user);
    
    /**
     * Delete all collaborations for a trip
     */
    void deleteByTrip(Trip trip);
    
    /**
     * Find recent collaborations by user
     */
    @Query("SELECT tc FROM TripCollaborator tc WHERE tc.user = :user " +
           "ORDER BY tc.joinedAt DESC, tc.invitedAt DESC")
    List<TripCollaborator> findRecentCollaborationsByUser(@Param("user") User user);
}