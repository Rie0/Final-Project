package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoachingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Scheduled time is mandatory")
    private LocalDateTime scheduledTime;

    @NotBlank(message = "Session type is mandatory")
    @Size(max = 100, message = "Session type should not exceed 100 characters")
    private String sessionType;

    @Size(max = 500, message = "Feedback should not exceed 500 characters")
    private String feedBack;

    @NotNull(message = "Pricing is mandatory")
    @Min(value = 0, message = "Pricing should not be less than 0")
    private Integer pricing;

    private boolean rescheduleRequested = false;

    private LocalDate newDate;

    private LocalTime newStartTime;

    private LocalTime newEndTime;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @NotNull(message = "Coach is mandatory")
    @JsonIgnore
    private Coach coach;


    @OneToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @NotNull(message = "Schedule is mandatory")
    @JsonIgnore
    private Schedule schedule;


}