package com.planora.planora.repository;

import com.planora.planora.entity.Trip;
import com.planora.planora.entity.TripStatus;
import com.planora.planora.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    /**
     * Find trips by owner
     */
    List<Trip> findByOwnerOrderByCreatedAtDesc(User owner);
    
    /**
     * Find trips by owner with pagination
     */
    Page<Trip> findByOwner(User owner, Pageable pageable);
    
    /**
     * Find trips by status
     */
    List<Trip> findByStatus(TripStatus status);
    
    /**
     * Find trips by destination (case-insensitive)
     */
    List<Trip> findByDestinationContainingIgnoreCase(String destination);
    
    /**
     * Find trips within date range
     */
    @Query("SELECT t FROM Trip t WHERE t.startDate >= :startDate AND t.endDate <= :endDate")
    List<Trip> findTripsInDateRange(@Param("startDate") LocalDate startDate, 
                                   @Param("endDate") LocalDate endDate);
    
    /**
     * Find upcoming trips for a user (as owner or collaborator)
     */
    @Query("SELECT DISTINCT t FROM Trip t " +
           "LEFT JOIN t.collaborators c " +
           "WHERE (t.owner = :user OR c.user = :user) " +
           "AND t.startDate >= :currentDate " +
           "AND t.status != 'CANCELLED' " +
           "ORDER BY t.startDate ASC")
    List<Trip> findUpcomingTripsForUser(@Param("user") User user, 
                                       @Param("currentDate") LocalDate currentDate);
    
    /**
     * Find past trips for a user
     */
    @Query("SELECT DISTINCT t FROM Trip t " +
           "LEFT JOIN t.collaborators c " +
           "WHERE (t.owner = :user OR c.user = :user) " +
           "AND t.endDate < :currentDate " +
           "ORDER BY t.endDate DESC")
    List<Trip> findPastTripsForUser(@Param("user") User user, 
                                   @Param("currentDate") LocalDate currentDate);
    
    /**
     * Find all trips for a user (as owner or collaborator)
     */
    @Query("SELECT DISTINCT t FROM Trip t " +
           "LEFT JOIN t.collaborators c " +
           "WHERE t.owner = :user OR c.user = :user " +
           "ORDER BY t.createdAt DESC")
    List<Trip> findAllTripsForUser(@Param("user") User user);
    
    /**
     * Find trips by owner and status
     */
    List<Trip> findByOwnerAndStatus(User owner, TripStatus status);
    
    /**
     * Find trips created after a specific date
     */
    List<Trip> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Count trips by owner
     */
    long countByOwner(User owner);
    
    /**
     * Find trips that need attention (starting soon without complete itinerary)
     */
    @Query("SELECT t FROM Trip t WHERE " +
           "t.startDate BETWEEN :startDate AND :endDate " +
           "AND t.status = 'PLANNING' " +
           "AND SIZE(t.itinerary) = 0")
    List<Trip> findTripsNeedingAttention(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
    
    /**
     * Search trips by title or destination
     */
    @Query("SELECT t FROM Trip t WHERE " +
           "LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.destination) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Trip> searchByTitleOrDestination(@Param("searchTerm") String searchTerm);
    
    /**
     * Find trips with collaborators
     */
    @Query("SELECT DISTINCT t FROM Trip t WHERE SIZE(t.collaborators) > 0")
    List<Trip> findTripsWithCollaborators();
    
    /**
     * Find trip by ID and verify user has access (owner or collaborator)
     */
    @Query("SELECT t FROM Trip t " +
           "LEFT JOIN t.collaborators c " +
           "WHERE t.id = :tripId AND (t.owner = :user OR c.user = :user)")
    Optional<Trip> findByIdAndUserHasAccess(@Param("tripId") Long tripId, 
                                           @Param("user") User user);
}