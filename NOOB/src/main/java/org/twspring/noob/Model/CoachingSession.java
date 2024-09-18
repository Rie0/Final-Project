package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


// Mohammed
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoachingSession {

    // GENERAL VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT")
    private Integer id;

    //@NotNull(message = "Start time is mandatory")
    private LocalDateTime startDate;

    //@NotNull(message = "End time is mandatory")
    private LocalDateTime endDate;

    @Size(max = 500, message = "Feedback should not exceed 500 characters")
    @Column(name = "feedback", length = 500, columnDefinition = "VARCHAR(500)")
    private String feedback;

    //@NotEmpty(message = "status is mandatory")
    @Column(name = "status", length = 500, columnDefinition = "VARCHAR(200)")
    private String status = "UPCOMING";

    // RELATIONSHIPS

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @JsonIgnore
    private Coach coach;

    @OneToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @JsonIgnore
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @OneToOne(mappedBy = "coachingSession", cascade = CascadeType.ALL)
    private Review review;
}
