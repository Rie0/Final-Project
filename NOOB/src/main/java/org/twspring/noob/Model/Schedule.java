package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Mohammed
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @JsonIgnore
    private Coach coach;

    @NotNull(message = "Start time is mandatory")
    private LocalDateTime startDate;

    @NotNull(message = "End time is mandatory")
    private LocalDateTime endDate;

    @NotNull(message = "Booking status is mandatory")
    private Boolean isBooked = false;





    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL)
    @JsonIgnore
    private CoachingSession coachingSession;




}