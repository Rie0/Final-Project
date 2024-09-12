package org.twspring.noob.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoachingSession {



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


    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @NotNull(message = "Coach is mandatory")
    private Coach coach;
}
