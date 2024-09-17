package org.twspring.noob.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    //RELATIONSHIP RELATED VARS

    //for team
    @CreationTimestamp
    @Column(columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime JoinedTeamAt;


    //RELATIONSHIPS
    @OneToOne
    @MapsId
    @JsonIgnore

    private User user;

    @ManyToOne
    @JsonIgnore
    private Team team;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private Set<TeamInvite> teamInvites;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private Set<SubscripeBy> subscripeBIES;



    //GAMES
}
