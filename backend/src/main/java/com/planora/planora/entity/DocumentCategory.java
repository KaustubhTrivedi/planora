package com.planora.planora.entity;

public enum DocumentCategory {
    BOOKING("Booking Confirmation"),
    TICKET("Ticket"),
    PASSPORT("Passport/ID"),
    VISA("Visa"),
    INSURANCE("Travel Insurance"),
    ITINERARY("Itinerary"),
    MAP("Map"),
    RECEIPT("Receipt"),
    PHOTO("Photo"),
    CHECKLIST("Checklist"),
    EMERGENCY("Emergency Contact"),
    MEDICAL("Medical Document"),
    OTHER("Other");
    
    private final String displayName;
    
    DocumentCategory(String displayName) {
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