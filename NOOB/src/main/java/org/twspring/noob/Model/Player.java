package org.twspring.noob.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    //VARIABLES

    //GENERAL VARS
    @Id
    private Integer id;

    @NotEmpty(message = "Bio cannot be empty")
    @NotBlank(message = "Bio cannot be blank")
    @Column(columnDefinition = "VARCHAR(150) NOT NULL")
    @Size(min= 25, max = 150, message = "Bio must have between 25 to 150 characters")
    private String bio;

//    @NotNull(message = "Gold wins must not be null")
//    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
//    @PositiveOrZero(message = "Gold wins cannot be a negative or a zero")
//    private int leaguesGoldWins;
//
//    @NotNull(message = "Silver wins must not be null")
//    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
//    @PositiveOrZero(message = "Silver wins cannot be a negative or a zero")
//    private int leaguesSilverWins;
//
//    @NotNull(message = "Bronze wins must not be null")
//    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
//    @PositiveOrZero(message = "Bronze wins cannot be a negative or a zero")
//    private int leaguesBronzeWins;

    //RELATIONSHIP RELATED VARS

    //for team
    @Column(columnDefinition = "timestamp")
    private LocalDateTime JoinedTeamAt;


    //RELATIONSHIPS
    @OneToOne
    @MapsId
    private User user;

    @ManyToOne
    @JsonIgnore
    private Team team;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private Set<TeamInvite> teamInvites;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    @JsonIgnore
    private Set<Participant> participants;

    //GAMES
}
