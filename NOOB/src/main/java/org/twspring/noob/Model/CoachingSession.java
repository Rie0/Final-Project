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

//    @NotBlank(message = "Session type is mandatory")
//    @Size(max = 100, message = "Session type should not exceed 100 characters")
//    @Column(name = "session_type", length = 100, nullable = false, columnDefinition = "VARCHAR(100)")
//    private String sessionStyle;

    @Size(max = 500, message = "Feedback should not exceed 500 characters")
    @Column(name = "feedback", length = 500, columnDefinition = "VARCHAR(500)")
    private String feedback;

    //@NotEmpty(message = "status is mandatory")
    @Column(name = "status", length = 500, columnDefinition = "VARCHAR(25)")
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
