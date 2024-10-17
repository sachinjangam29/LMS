package com.jangam.schoolmgt.userservice.payload.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String password;
}
