package com.planora.planora.dto.auth;

import jakarta.validation.constraints.Size;

/**
 * DTO for updating user profile
 */
public class UpdateProfileRequest {
    
    @Size(max = 50, message = "First name should not exceed 50 characters")
    private String firstName;
    
    @Size(max = 50, message = "Last name should not exceed 50 characters")
    private String lastName;
    
    // Constructors
    public UpdateProfileRequest() {}
    
    public UpdateProfileRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    @Override
    public String toString() {
        return "UpdateProfileRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}