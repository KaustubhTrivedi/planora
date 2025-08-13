package com.planora.planora.repository;

import com.planora.planora.entity.Trip;
import com.planora.planora.entity.TripBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripBudgetRepository extends JpaRepository<TripBudget, Long> {
    
    /**
     * Find budget by trip
     */
    Optional<TripBudget> findByTrip(Trip trip);
    
    /**
     * Find budget by trip ID
     */
    @Query("SELECT tb FROM TripBudget tb WHERE tb.trip.id = :tripId")
    Optional<TripBudget> findByTripId(@Param("tripId") Long tripId);
    
    /**
     * Find budgets by currency
     */
    List<TripBudget> findByCurrency(String currency);
    
    /**
     * Find budgets with total budget greater than specified amount
     */
    List<TripBudget> findByTotalBudgetGreaterThan(BigDecimal amount);
    
    /**
     * Find budgets with total budget less than specified amount
     */
    List<TripBudget> findByTotalBudgetLessThan(BigDecimal amount);
    
    /**
     * Find budgets within a range
     */
    @Query("SELECT tb FROM TripBudget tb WHERE tb.totalBudget >= :minAmount AND tb.totalBudget <= :maxAmount")
    List<TripBudget> findByTotalBudgetBetween(@Param("minAmount") BigDecimal minAmount,
                                             @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * Find over-budget trips
     */
    @Query("SELECT tb FROM TripBudget tb WHERE tb.actualSpent > tb.totalBudget")
    List<TripBudget> findOverBudgetTrips();
    
    /**
     * Find under-budget trips
     */
    @Query("SELECT tb FROM TripBudget tb WHERE tb.actualSpent < tb.totalBudget")
    List<TripBudget> findUnderBudgetTrips();
    
    /**
     * Find budgets with no spending recorded
     */
    @Query("SELECT tb FROM TripBudget tb WHERE tb.actualSpent = 0 OR tb.actualSpent IS NULL")
    List<TripBudget> findBudgetsWithNoSpending();
    
    /**
     * Calculate average total budget
     */
    @Query("SELECT AVG(tb.totalBudget) FROM TripBudget tb WHERE tb.totalBudget IS NOT NULL")
    BigDecimal calculateAverageTotalBudget();
    
    /**
     * Calculate average actual spending
     */
    @Query("SELECT AVG(tb.actualSpent) FROM TripBudget tb WHERE tb.actualSpent IS NOT NULL")
    BigDecimal calculateAverageActualSpending();
    
    /**
     * Find budgets by accommodation budget range
     */
    @Query("SELECT tb FROM TripBudget tb WHERE tb.accommodationBudget >= :minAmount " +
           "AND tb.accommodationBudget <= :maxAmount")
    List<TripBudget> findByAccommodationBudgetBetween(@Param("minAmount") BigDecimal minAmount,
                                                     @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * Find budgets by transportation budget range
     */
    @Query("SELECT tb FROM TripBudget tb WHERE tb.transportationBudget >= :minAmount " +
           "AND tb.transportationBudget <= :maxAmount")
    List<TripBudget> findByTransportationBudgetBetween(@Param("minAmount") BigDecimal minAmount,
                                                      @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * Check if budget exists for trip
     */
    boolean existsByTrip(Trip trip);
    
    /**
     * Count budgets by currency
     */
    long countByCurrency(String currency);
    
    /**
     * Find budgets with complete category breakdown
     */
    @Query("SELECT tb FROM TripBudget tb WHERE tb.accommodationBudget IS NOT NULL " +
           "AND tb.transportationBudget IS NOT NULL " +
           "AND tb.foodBudget IS NOT NULL " +
           "AND tb.activitiesBudget IS NOT NULL")
    List<TripBudget> findBudgetsWithCompleteCategoryBreakdown();
    
    /**
     * Find budgets with incomplete category breakdown
     */
    @Query("SELECT tb FROM TripBudget tb WHERE tb.accommodationBudget IS NULL " +
           "OR tb.transportationBudget IS NULL " +
           "OR tb.foodBudget IS NULL " +
           "OR tb.activitiesBudget IS NULL")
    List<TripBudget> findBudgetsWithIncompleteCategoryBreakdown();
    
    /**
     * Delete budget by trip
     */
    void deleteByTrip(Trip trip);
    
    /**
     * Find budgets ordered by total budget descending
     */
    List<TripBudget> findAllByOrderByTotalBudgetDesc();
    
    /**
     * Find budgets ordered by actual spent descending
     */
    List<TripBudget> findAllByOrderByActualSpentDesc();
}