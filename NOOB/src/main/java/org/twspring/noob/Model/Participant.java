package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Hussam

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

//Hussam
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Unique identifier for the participant

    @NotEmpty(message = "Name cannot be empty")
    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String name; // Name of the participant (player or team)

    @Column(columnDefinition = "INT")
    private Integer seed=0; // Seed number of the participant for bracket placement


    @Column(columnDefinition = "INT")
    private int prize =0; // prize for the participant

    @Column(columnDefinition = "INT DEFAULT 0")
    private int score = 0;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null")
    private Status status= Status.REGISTERED;

    public enum Status{
        REGISTERED,//Player is registered
        CHECKED_IN,
        ACTIVE, //player is in an ongoing tournament/league
        ELIMINATED,
        FINALIZED
    }

    @ManyToOne
    @JoinColumn(name = "player_id")
    @JsonIgnore
    private Player player; // Reference to the Player associated with this participant

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    @JsonIgnore
    private Tournament tournament; // Reference to the Tournament this participant is associated with

    @ManyToOne
    @JsonIgnore
    private League league; // Reference to the League this participant is associated with
}
