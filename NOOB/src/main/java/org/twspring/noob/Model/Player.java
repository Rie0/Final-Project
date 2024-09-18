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
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player { //Rafeef

    //VARIABLES

    //GENERAL VARS
    @Id
    private Integer id;

    @Column(columnDefinition = "VARCHAR(150) NOT NULL")
    @NotNull(message = "bio cannot be null")
    @Size(max = 150, message = "Bio must have between 0 to 150 characters")
    private String bio ="";


    @Column(columnDefinition = "VARCHAR(150)") //only for minors (under 18)
    private String parentApproval;

    //RELATIONSHIP RELATED VARS

    //for team
    @Column(columnDefinition = "DATE")
    private LocalDateTime JoinedTeamAt;


    //RELATIONSHIPS
    //Rafeef
    @OneToOne
    @MapsId
    @JsonIgnore

    private User user;

    @ManyToOne
    @JsonIgnore
    private Team team;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    @JsonIgnore
    private Set<TeamInvite> teamInvites;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    @JsonIgnore
    private Set<Participant> participants;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<Review> reviews;

    //hassan
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private Set<SubscripeBy> subscripeBIES;



}
