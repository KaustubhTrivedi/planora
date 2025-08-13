package com.planora.planora.entity;

public enum CollaboratorRole {
    VIEWER("Viewer", "Can view trip details and itinerary"),
    EDITOR("Editor", "Can edit trip details and itinerary"),
    ADMIN("Admin", "Can edit trip and manage collaborators");
    
    private final String displayName;
    private final String description;
    
    CollaboratorRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}