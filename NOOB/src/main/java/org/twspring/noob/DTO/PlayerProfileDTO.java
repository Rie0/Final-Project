package org.twspring.noob.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PlayerProfileDTO {

    private String username;

    private Integer age;

    private String bio;

    private String teamName;

    private LocalDateTime joinDate;
}
