package com.bersyte.eventz.features.users.domain.model;

import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.features.auth.domain.exceptions.InvalidVerificationCodeException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class AppUser {
    private final UUID id;
    private final String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private boolean enabled;
    private boolean verified;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Security/auth
    private String verificationCode;
    private String password;
    private LocalDateTime verificationExpiration;
    private String recoveryCode;
    private LocalDateTime recoveryCodeExpiresAt;
    
    
    private AppUser(UUID id, String email, String firstName,
                    String lastName, String phone, UserRole role, boolean enabled,
                    boolean verified, LocalDateTime createdAt, LocalDateTime updatedAt,
                    String verificationCode, String password, LocalDateTime verificationExpiration,
                    String recoveryCode, LocalDateTime recoveryCodeExpiresAt
    ) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.enabled = enabled;
        this.verified = verified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.recoveryCode = recoveryCode;
        this.password = password;
        this.verificationExpiration= verificationExpiration;
        this.verificationCode = verificationCode;
        this.recoveryCodeExpiresAt = recoveryCodeExpiresAt;
    }
    
    /**
     * Factory method for CREATING a new user.
     * Defines the initial state: role, and enabled status.
     */
    public static AppUser create(
            UUID id, String email, String firstName, String lastName,
            String phone, LocalDateTime createdAt,  String verificationCode,
            String password, LocalDateTime verificationExpiration
    ) {
        return new AppUser(
                id, email, firstName, lastName, phone, UserRole.USER,
                true, false,createdAt, createdAt,
                verificationCode,password,verificationExpiration, null, null
        );
    }
    
    /**
     * Factory Method to RESTORE an existing user (coming from Infrastructure/Database).
     */
    public static AppUser restore(
            UUID id, String email, String firstName, String lastName, String phone,
            UserRole role, boolean enabled,boolean verified, LocalDateTime createdAt,
            LocalDateTime updatedAt, String verificationCode, String password,
            LocalDateTime verificationExpiration, String recoveryCode,LocalDateTime recoveryCodeExpiresAt
    ) {
        return new AppUser(id, email, firstName, lastName, phone, role, enabled,verified, createdAt, updatedAt,
               verificationCode, password,verificationExpiration,recoveryCode,recoveryCodeExpiresAt
        );
    }
    
    public AppUser updateVerificationCode(
             LocalDateTime updatedAt,
             String verificationCode,
             LocalDateTime verificationExpiration
    ) {
      if (this.isVerified()){
            throw new AuthException("User already verified");
      }
      this.verificationCode = verificationCode;
      this.verificationExpiration = verificationExpiration;
      this.updatedAt = updatedAt;
      return this;
    }
    
    
    public boolean isAdmin(){
        return this.role == UserRole.ADMIN;
    }
    
    public boolean isOrganizer(){
        return this.role == UserRole.ORGANIZER;
    }
    
    public boolean canDeleteOrUpdateUser(UUID targetId){
        return this.canManageEvents() || this.id.equals(targetId);
    }
    
    
    public boolean canManageEvents(){
        return this.isAdmin() || this.isOrganizer();
    }
    
    
    public boolean canSeeUserDetails(UUID targetId){
        return this.isAdmin() || this.id.equals(targetId);
    }
    
    
    public AppUser requestRecoveryCode(
            String code, LocalDateTime now, Duration validity
    ){
        if(this.recoveryCode != null){
            LocalDateTime threshold = now.minusMinutes(1);
            
            if(this.updatedAt.isBefore(threshold)){
                throw new AuthException("Please wait a moment before requesting a new code.");
            }
        }
        this.recoveryCode = code;
        this.verificationExpiration = now.plus(validity);
        this.updatedAt = now;
        return this;
    }
    
    public AppUser changePassword(
            String recoveryCode, String newPasswordEncoded, LocalDateTime updatedAt
    ){
        if(!this.recoveryCode.equals(recoveryCode)){
            throw new AuthException("Invalid request");
        }
        this.password = newPasswordEncoded;
        this.recoveryCode= null;
        this.recoveryCodeExpiresAt = null;
        this.updatedAt = updatedAt;
        return this;
    }
   
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    
    public boolean isVerificationCodeExpired(LocalDateTime verificationTime){
        return verificationTime.isAfter(this.verificationExpiration);
    }
    
    public boolean isVerified() {
        return verified;
    }
    
    public AppUser verifyCode(String givenCode, LocalDateTime updatedAt){
        boolean isVerified = this.verificationCode.equals(givenCode);
        if(!isVerified){
            throw new InvalidVerificationCodeException("Invalid verification code");
        }
        this.verified = true;
        this.verificationExpiration = null;
        this.verificationCode = null;
        this.updatedAt = updatedAt;
        return this;
    }
    
    public UUID getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public String getVerificationCode() {
        return verificationCode;
    }
    
    public String getPassword() {
        return password;
    }
    
    public LocalDateTime getVerificationExpiration() {
        return verificationExpiration;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
