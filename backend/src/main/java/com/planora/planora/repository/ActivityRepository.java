package com.planora.planora.repository;

import com.planora.planora.entity.Activity;
import com.planora.planora.entity.ActivityType;
import com.planora.planora.entity.DayItinerary;
import com.planora.planora.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    
    /**
     * Find activities by day itinerary, ordered by start time
     */
    List<Activity> findByDayItineraryOrderByStartTimeAsc(DayItinerary dayItinerary);
    
    /**
     * Find activities by activity type
     */
    List<Activity> findByType(ActivityType type);
    
    /**
     * Find activities by day itinerary and type
     */
    List<Activity> findByDayItineraryAndType(DayItinerary dayItinerary, ActivityType type);
    
    /**
     * Find activities by location (case-insensitive)
     */
    List<Activity> findByLocationContainingIgnoreCase(String location);
    
    /**
     * Find activities with estimated cost greater than specified amount
     */
    List<Activity> findByEstimatedCostGreaterThan(BigDecimal amount);
    
    /**
     * Find activities with actual cost
     */
    List<Activity> findByActualCostIsNotNull();
    
    /**
     * Find activities by trip
     */
    @Query("SELECT a FROM Activity a WHERE a.dayItinerary.trip = :trip " +
           "ORDER BY a.dayItinerary.date ASC, a.startTime ASC")
    List<Activity> findByTrip(@Param("trip") Trip trip);
    
    /**
     * Find activities by trip and type
     */
    @Query("SELECT a FROM Activity a WHERE a.dayItinerary.trip = :trip AND a.type = :type " +
           "ORDER BY a.dayItinerary.date ASC, a.startTime ASC")
    List<Activity> findByTripAndType(@Param("trip") Trip trip, @Param("type") ActivityType type);
    
    /**
     * Find activities with time conflicts in the same day itinerary
     */
    @Query("SELECT a FROM Activity a WHERE a.dayItinerary = :dayItinerary " +
           "AND a.id != :activityId " +
           "AND a.startTime IS NOT NULL AND a.endTime IS NOT NULL " +
           "AND NOT (a.endTime <= :startTime OR a.startTime >= :endTime)")
    List<Activity> findConflictingActivities(@Param("dayItinerary") DayItinerary dayItinerary,
                                           @Param("activityId") Long activityId,
                                           @Param("startTime") LocalTime startTime,
                                           @Param("endTime") LocalTime endTime);
    
    /**
     * Find activities with booking references
     */
    @Query("SELECT a FROM Activity a WHERE a.bookingReference IS NOT NULL AND a.bookingReference != ''")
    List<Activity> findActivitiesWithBookings();
    
    /**
     * Calculate total estimated cost for a trip
     */
    @Query("SELECT COALESCE(SUM(a.estimatedCost), 0) FROM Activity a WHERE a.dayItinerary.trip = :trip")
    BigDecimal calculateTotalEstimatedCostForTrip(@Param("trip") Trip trip);
    
    /**
     * Calculate total actual cost for a trip
     */
    @Query("SELECT COALESCE(SUM(a.actualCost), 0) FROM Activity a WHERE a.dayItinerary.trip = :trip " +
           "AND a.actualCost IS NOT NULL")
    BigDecimal calculateTotalActualCostForTrip(@Param("trip") Trip trip);
    
    /**
     * Find activities by place ID
     */
    List<Activity> findByPlaceId(String placeId);
    
    /**
     * Count activities by day itinerary
     */
    long countByDayItinerary(DayItinerary dayItinerary);
    
    /**
     * Count activities by trip
     */
    @Query("SELECT COUNT(a) FROM Activity a WHERE a.dayItinerary.trip = :trip")
    long countByTrip(@Param("trip") Trip trip);
    
    /**
     * Find activities by name (case-insensitive)
     */
    List<Activity> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find activities with notes
     */
    @Query("SELECT a FROM Activity a WHERE a.notes IS NOT NULL AND a.notes != ''")
    List<Activity> findActivitiesWithNotes();
    
    /**
     * Delete all activities for a day itinerary
     */
    void deleteByDayItinerary(DayItinerary dayItinerary);
    
    /**
     * Find activities by trip ID
     */
    @Query("SELECT a FROM Activity a WHERE a.dayItinerary.trip.id = :tripId " +
           "ORDER BY a.dayItinerary.date ASC, a.startTime ASC")
    List<Activity> findByTripId(@Param("tripId") Long tripId);
}