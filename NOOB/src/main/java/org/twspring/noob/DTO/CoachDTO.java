package org.twspring.noob.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

// Mohammed
@Data
@AllArgsConstructor
public class CoachDTO {




    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;

    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+9665\\d{8}$", message = "Phone number must be a valid Saudi phone number starts with +9665")
    private String phoneNumber;

    @NotEmpty(message = "Bio cannot be empty")
    private String bio;

    @NotEmpty(message = "Expertise cannot be empty")
    private String expertise;

    @NotNull(message = "Hourly rate cannot be null")
    @Positive(message = "Hourly rate should be positive")
    private Integer hourlyRate;


}
