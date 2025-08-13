package com.planora.planora.entity;

public enum ActivityType {
    ACCOMMODATION("Accommodation"),
    TRANSPORTATION("Transportation"),
    SIGHTSEEING("Sightseeing"),
    DINING("Dining"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    OUTDOOR("Outdoor Activity"),
    CULTURAL("Cultural Activity"),
    BUSINESS("Business"),
    RELAXATION("Relaxation"),
    ADVENTURE("Adventure"),
    EDUCATION("Educational"),
    OTHER("Other");
    
    private final String displayName;
    
    ActivityType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}