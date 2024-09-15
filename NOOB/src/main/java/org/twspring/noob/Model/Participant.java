package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Unique identifier for the participant

    @NotEmpty(message = "Name cannot be empty")
    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String name; // Name of the participant (player or team)

    @Column(columnDefinition = "INT")
    private Integer seed; // Seed number of the participant for bracket placement


    @Column(columnDefinition = "INT")
    private int prize =0; // prize for the participant


    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String status = "REGISTERED"; // Status of the participant (e.g., "ACTIVE", "ELIMINATED")

    @ManyToOne
    @JoinColumn(name = "player_id")
    @JsonIgnore

    private Player player; // Reference to the Player associated with this participant

    @ManyToOne
    @JoinColumn(name = "tournament_id", columnDefinition = "INT")
    @JsonIgnore

    private Tournament tournament; // Reference to the Tournament this participant is associated with


    @OneToMany(mappedBy = "participant1", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Match> matchesAsParticipant1; // List of matches where this participant is 'participant1'

    @OneToMany(mappedBy = "participant2", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Match> matchesAsParticipant2; // List of matches where this participant is 'participant2'
}
