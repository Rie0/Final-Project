package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

@Table(name = "game_match") // Renaming the table to avoid reserved keyword issues

public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "participant1_id")
    @JsonIgnore
    private Participant participant1; // Reference to the first participant

    @ManyToOne
    @JoinColumn(name = "participant2_id")
    @JsonIgnore
    private Participant participant2; // Reference to the second participant

    @ManyToOne
    @JoinColumn(name = "winner_id")
    @JsonIgnore
    private Participant winner; // Reference to the winning participant

    @ManyToOne
    @JoinColumn(name = "loser_id")
    @JsonIgnore
    private Participant loser; // Reference to the losing participant


    @Column(name = "participant1_ready", nullable = false)
    private boolean participant1Ready = false;

    @Column(name = "participant2_ready", nullable = false)
    private boolean participant2Ready = false;


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime; // Start time of the match

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime; // End time of the match

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String status; // Status of the match (e.g., "PENDING", "IN_PROGRESS", "COMPLETED")

//    @Column(columnDefinition = "VARCHAR(20)")
//    private String score; // Score of the match (e.g., "2-1")

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer participant1score = 0;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer participant2score = 0;

    @Column(columnDefinition = "VARCHAR(50) ")
    private String participant1Name;

    @Column(columnDefinition = "VARCHAR(50) ")
    private String participant2Name;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    @JsonIgnore
    private Tournament tournament; // Reference to the Tournament this match belongs to

    @ManyToOne
    @JoinColumn(name = "round_id")
    @JsonIgnore
    private Round round; // Reference to the Round this match is part of

    //rafeef
    @ManyToOne
    @JsonIgnore
    private League league;
}
