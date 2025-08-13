package com.planora.planora.entity;

public enum PreferenceType {
    STRING("String"),
    INTEGER("Integer"),
    DECIMAL("Decimal"),
    BOOLEAN("Boolean"),
    JSON("JSON");
    
    private final String displayName;
    
    PreferenceType(String displayName) {
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