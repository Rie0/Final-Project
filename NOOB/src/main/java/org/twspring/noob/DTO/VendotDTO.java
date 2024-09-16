package org.twspring.noob.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VendotDTO {


    @NotEmpty(message = "Username cannot be empty")
    @Size(min=4,max = 10,
            message = "Username must have between 4 to 10 characters")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{6,}$",
            message = "Password must be strong (at least: at least 6 characters, one uppercase letter, one lowercase letter, one number, and one special character)")
    private String password;


    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email must have a valid format")
    @Size(min=7,max = 50,
            message = "Email must have between 7 to 50 letters")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+9665\\d{8}$",
            message = "Phone number must be a valid Saudi phone number")
    private String phoneNumber;

    @Past(message = "Birthdate cannot be in the past")
    @NotNull(message = "Birthday cannot be null")
    private LocalDate birthday;

    @CreationTimestamp
    private final LocalDateTime joinedAt = LocalDateTime.now();
    @NotEmpty(message = "Zone Capacity   can not be empty")
    @Column(columnDefinition = "int not null")
    private String VenueName;

}
