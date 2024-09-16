package org.twspring.noob.DTO;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DateTimeDTO {
    @NotNull(message = "start date cannot be empty")
    private LocalDateTime StartDate;
    @NotNull(message = "end date cannot be empty")
    private LocalDateTime EndDate;
}
