package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String status; // Status of the participant (e.g., "ACTIVE", "ELIMINATED")

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player; // Reference to the Player associated with this participant

    @ManyToOne
    @JoinColumn(name = "tournament_id", columnDefinition = "INT")
    private Tournament tournament; // Reference to the Tournament this participant is associated with

    @ManyToOne
    private League league; // Reference to the League this participant is associated with



}
