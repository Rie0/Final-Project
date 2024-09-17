package org.twspring.noob.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team {     //rafeef

    //VARIABLES

    //AUTHENTICATION VARS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(150) NOT NULL")
    @NotNull(message = "bio cannot be null")
    @Size(max = 150, message = "Bio must have between 0 to 150 characters")
    private String bio ="";

    @NotNull(message = "Winnings must not be null")
    @Column(columnDefinition = "DOUBLE NOT NULL DEFAULT 0")
    @PositiveOrZero(message = "Winnings amount cannot be a negative or a zero")
    private double winnings;

    //RELATIONSHIPS
    @OneToOne
    @MapsId
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private Set<Player> players;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private Set<TeamInvite> teamInvites;

    //future work
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
//    @JsonIgnore
//    private Set<Participant> participants;
}
