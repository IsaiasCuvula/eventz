package com.bersyte.eventz.features.users.domain.model;

import java.time.LocalDateTime;

public class AppUser {
    private final String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private boolean enabled;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Security/auth
    private String verificationCode;
    private String password;
    private LocalDateTime verificationExpiration;
    
    
    
    private AppUser(String id, String email, String firstName, String lastName, String phone,
                    UserRole role, boolean enabled, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    /**
     * Factory method for CREATING a new user.
     * Defines the initial state: generated ID, creation dates, and enabled status.
     */
    public static AppUser create(String id, String email, String firstName, String lastName, String phone) {
        return new AppUser(
                id,
                email,
                firstName,
                lastName,
                phone,
                UserRole.USER,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
    
    /**
     * Factory Method to RESTORE an existing user (coming from Infrastructure/Database).
     */
    public static AppUser restore(String id, String email, String firstName, String lastName, String phone,
                                  UserRole role, boolean enabled, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new AppUser(id, email, firstName, lastName, phone, role, enabled, createdAt, updatedAt);
    }
    
    
    public boolean isAdmin(){
        return this.role == UserRole.ADMIN;
    }
    
    public boolean isOrganizer(){
        return this.role == UserRole.ORGANIZER;
    }
    
    public boolean canManageEvents(){
        
        return this.isAdmin() || this.isOrganizer();
    }
    
    
    public boolean canSeeUserDetails(String targetId){
        return this.isAdmin() || this.id.equals(targetId);
    }
    
    public boolean canDeleteOrUpdateUser(String targetId){
        return this.isAdmin() || this.id.equals(targetId);
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    
    public String getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getVerificationCode() {
        return verificationCode;
    }
    
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public LocalDateTime getVerificationExpiration() {
        return verificationExpiration;
    }
    
    public void setVerificationExpiration(LocalDateTime verificationExpiration) {
        this.verificationExpiration = verificationExpiration;
    }
    
    
}
