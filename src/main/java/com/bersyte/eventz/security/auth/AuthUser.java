package com.bersyte.eventz.security.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class AuthUser {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
