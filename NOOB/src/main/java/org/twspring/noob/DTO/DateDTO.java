package org.twspring.noob.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class DateDTO { //rafeef
    @NotNull(message = "start date cannot be empty")
    private LocalDate StartDate;
    @NotNull(message = "end date cannot be empty")
    private LocalDate EndDate;
}
