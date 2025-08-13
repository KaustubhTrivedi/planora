package com.planora.planora.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_budgets", indexes = {
    @Index(name = "idx_trip_budget_trip", columnList = "trip_id", unique = true)
})
public class TripBudget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "total_budget", precision = 12, scale = 2)
    @PositiveOrZero(message = "Total budget must be positive or zero")
    private BigDecimal totalBudget;
    
    @Column(name = "accommodation_budget", precision = 12, scale = 2)
    @PositiveOrZero(message = "Accommodation budget must be positive or zero")
    private BigDecimal accommodationBudget;
    
    @Column(name = "transportation_budget", precision = 12, scale = 2)
    @PositiveOrZero(message = "Transportation budget must be positive or zero")
    private BigDecimal transportationBudget;
    
    @Column(name = "food_budget", precision = 12, scale = 2)
    @PositiveOrZero(message = "Food budget must be positive or zero")
    private BigDecimal foodBudget;
    
    @Column(name = "activities_budget", precision = 12, scale = 2)
    @PositiveOrZero(message = "Activities budget must be positive or zero")
    private BigDecimal activitiesBudget;
    
    @Column(name = "shopping_budget", precision = 12, scale = 2)
    @PositiveOrZero(message = "Shopping budget must be positive or zero")
    private BigDecimal shoppingBudget;
    
    @Column(name = "miscellaneous_budget", precision = 12, scale = 2)
    @PositiveOrZero(message = "Miscellaneous budget must be positive or zero")
    private BigDecimal miscellaneousBudget;
    
    @Column(name = "actual_spent", precision = 12, scale = 2)
    @PositiveOrZero(message = "Actual spent must be positive or zero")
    private BigDecimal actualSpent = BigDecimal.ZERO;
    
    @Column(name = "currency", length = 3)
    private String currency = "USD";
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @NotNull(message = "Trip is required")
    private Trip trip;
    
    // Constructors
    public TripBudget() {}
    
    public TripBudget(Trip trip) {
        this.trip = trip;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BigDecimal getTotalBudget() {
        return totalBudget;
    }
    
    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }
    
    public BigDecimal getAccommodationBudget() {
        return accommodationBudget;
    }
    
    public void setAccommodationBudget(BigDecimal accommodationBudget) {
        this.accommodationBudget = accommodationBudget;
    }
    
    public BigDecimal getTransportationBudget() {
        return transportationBudget;
    }
    
    public void setTransportationBudget(BigDecimal transportationBudget) {
        this.transportationBudget = transportationBudget;
    }
    
    public BigDecimal getFoodBudget() {
        return foodBudget;
    }
    
    public void setFoodBudget(BigDecimal foodBudget) {
        this.foodBudget = foodBudget;
    }
    
    public BigDecimal getActivitiesBudget() {
        return activitiesBudget;
    }
    
    public void setActivitiesBudget(BigDecimal activitiesBudget) {
        this.activitiesBudget = activitiesBudget;
    }
    
    public BigDecimal getShoppingBudget() {
        return shoppingBudget;
    }
    
    public void setShoppingBudget(BigDecimal shoppingBudget) {
        this.shoppingBudget = shoppingBudget;
    }
    
    public BigDecimal getMiscellaneousBudget() {
        return miscellaneousBudget;
    }
    
    public void setMiscellaneousBudget(BigDecimal miscellaneousBudget) {
        this.miscellaneousBudget = miscellaneousBudget;
    }
    
    public BigDecimal getActualSpent() {
        return actualSpent;
    }
    
    public void setActualSpent(BigDecimal actualSpent) {
        this.actualSpent = actualSpent;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
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
    
    // Utility methods
    public BigDecimal calculateTotalCategoryBudgets() {
        BigDecimal total = BigDecimal.ZERO;
        if (accommodationBudget != null) total = total.add(accommodationBudget);
        if (transportationBudget != null) total = total.add(transportationBudget);
        if (foodBudget != null) total = total.add(foodBudget);
        if (activitiesBudget != null) total = total.add(activitiesBudget);
        if (shoppingBudget != null) total = total.add(shoppingBudget);
        if (miscellaneousBudget != null) total = total.add(miscellaneousBudget);
        return total;
    }
    
    public BigDecimal getRemainingBudget() {
        if (totalBudget == null) return null;
        BigDecimal spent = actualSpent != null ? actualSpent : BigDecimal.ZERO;
        return totalBudget.subtract(spent);
    }
    
    public double getBudgetUtilizationPercentage() {
        if (totalBudget == null || totalBudget.compareTo(BigDecimal.ZERO) == 0) return 0.0;
        BigDecimal spent = actualSpent != null ? actualSpent : BigDecimal.ZERO;
        return spent.divide(totalBudget, 4, RoundingMode.HALF_UP)
                   .multiply(BigDecimal.valueOf(100))
                   .doubleValue();
    }
    
    public boolean isOverBudget() {
        if (totalBudget == null) return false;
        BigDecimal spent = actualSpent != null ? actualSpent : BigDecimal.ZERO;
        return spent.compareTo(totalBudget) > 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripBudget)) return false;
        TripBudget that = (TripBudget) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "TripBudget{" +
                "id=" + id +
                ", totalBudget=" + totalBudget +
                ", actualSpent=" + actualSpent +
                ", currency='" + currency + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}