package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

// Mohammed
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Rating is mandatory")
    @Min(value = 0, message = "Rating should not be less than 0")
    @Max(value = 5, message = "Rating should not be more than 5")
    private Float rating;

    @NotNull(message = "Comment is mandatory")
    private String comment;

    // Relationship with User
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    @NotNull(message = "User is mandatory")
    @JsonIgnore

    private Player player;

    // Relationship with Coach
    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @NotNull(message = "Coach is mandatory")
    @JsonIgnore
    private Coach coach;

    // Relationship with CoachingSession
    @OneToOne
    @JoinColumn(name = "session_id", nullable = false)
    @NotNull(message = "Coaching session is mandatory")
    @JsonIgnore
    private CoachingSession coachingSession;

}
