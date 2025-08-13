package com.planora.planora.repository;

import com.planora.planora.entity.DayItinerary;
import com.planora.planora.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DayItineraryRepository extends JpaRepository<DayItinerary, Long> {
    
    /**
     * Find day itineraries by trip, ordered by date
     */
    List<DayItinerary> findByTripOrderByDateAsc(Trip trip);
    
    /**
     * Find day itinerary by trip and date
     */
    Optional<DayItinerary> findByTripAndDate(Trip trip, LocalDate date);
    
    /**
     * Find day itinerary by trip and day number
     */
    Optional<DayItinerary> findByTripAndDayNumber(Trip trip, Integer dayNumber);
    
    /**
     * Find day itineraries by trip within date range
     */
    @Query("SELECT di FROM DayItinerary di WHERE di.trip = :trip " +
           "AND di.date >= :startDate AND di.date <= :endDate " +
           "ORDER BY di.date ASC")
    List<DayItinerary> findByTripAndDateRange(@Param("trip") Trip trip,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);
    
    /**
     * Count day itineraries by trip
     */
    long countByTrip(Trip trip);
    
    /**
     * Find day itineraries with activities
     */
    @Query("SELECT di FROM DayItinerary di WHERE di.trip = :trip AND SIZE(di.activities) > 0 " +
           "ORDER BY di.date ASC")
    List<DayItinerary> findByTripWithActivities(@Param("trip") Trip trip);
    
    /**
     * Find day itineraries without activities
     */
    @Query("SELECT di FROM DayItinerary di WHERE di.trip = :trip AND SIZE(di.activities) = 0 " +
           "ORDER BY di.date ASC")
    List<DayItinerary> findByTripWithoutActivities(@Param("trip") Trip trip);
    
    /**
     * Find day itineraries by trip ID
     */
    @Query("SELECT di FROM DayItinerary di WHERE di.trip.id = :tripId ORDER BY di.date ASC")
    List<DayItinerary> findByTripId(@Param("tripId") Long tripId);
    
    /**
     * Find day itineraries with notes
     */
    @Query("SELECT di FROM DayItinerary di WHERE di.trip = :trip " +
           "AND di.notes IS NOT NULL AND di.notes != '' " +
           "ORDER BY di.date ASC")
    List<DayItinerary> findByTripWithNotes(@Param("trip") Trip trip);
    
    /**
     * Find the first day itinerary for a trip
     */
    Optional<DayItinerary> findFirstByTripOrderByDateAsc(Trip trip);
    
    /**
     * Find the last day itinerary for a trip
     */
    Optional<DayItinerary> findFirstByTripOrderByDateDesc(Trip trip);
    
    /**
     * Check if day itinerary exists for trip and date
     */
    boolean existsByTripAndDate(Trip trip, LocalDate date);
    
    /**
     * Delete all day itineraries for a trip
     */
    void deleteByTrip(Trip trip);
}