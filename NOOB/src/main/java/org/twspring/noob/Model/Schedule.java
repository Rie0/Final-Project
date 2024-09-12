package org.twspring.noob.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;

    @NotNull(message = "Coach ID is mandatory")
    private Integer coachId;

    @NotNull(message = "Start time is mandatory")
    private LocalDate startTime;

    @NotNull(message = "End time is mandatory")
    private LocalDate endTime;

    @NotNull(message = "Booking status is mandatory")
    private Boolean isBooked;

}
