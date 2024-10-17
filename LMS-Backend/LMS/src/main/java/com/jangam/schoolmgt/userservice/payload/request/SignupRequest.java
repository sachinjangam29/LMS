package com.jangam.schoolmgt.userservice.payload.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jangam.schoolmgt.userservice.JSON.deseralize.RoleDeserializer;
import com.jangam.schoolmgt.userservice.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class SignupRequest {
    @Column(name = "username")
    @NotBlank
    @Size(max = 50)
    private String username;

    @Column(name = "password")
    @NotBlank
    @Size(max = 255)
    private String password;

    @Column(name = "first_name")
    @NotBlank
    @Size(max = 35)
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    @Size(max = 35)
    private String lastName;

    @Column(name = "email")
    @NotBlank
    @Size(max = 254)
    @Email
    private String email;

    @Column(name = "phone_number")
    @NotBlank
    @Size(max = 15)
    private String phoneNumber;

    @JsonDeserialize(using = RoleDeserializer.class)
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_active")
    private Boolean isActive;
}
