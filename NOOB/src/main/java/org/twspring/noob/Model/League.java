package org.twspring.noob.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class League { //RAFEEF

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    @Size(min = 2, max = 100, message = "Name must have between 2 to 100 characters")
    private String name;

    @NotNull(message = "Description cannot be null")
    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDate startDate;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status status= Status.INACTIVE;

    public enum Status{
        INACTIVE, //CREATED BUT DATES AREN'T SPECIFIED
        OPEN, //PLAYERS CAN NOW PARTICIPATE
        FULL, //PARTICIPANTS ARE MAXED, NO PLAYERS CAN JOIN BUT CAN STILL WITHDRAW
        ONGOING, //LEAGUE STARTED, MATCHES ARE NOW TAKING PLACE
        ENDED //LEAGUE ENDED
    }

    @NotNull(message = "Location cannot be null")
    @Column(columnDefinition = "VARCHAR(255)")
    private String location;

    @NotNull(message = "Maximum participants must not be null")
    @Min(value = 1, message = "Maximum participants must be at least 1")
    @Column(columnDefinition = "INT NOT NULL")
    private Integer maxParticipants;

    @Min(value = 0, message = "Current participants cannot be negative")
    @Column(columnDefinition = "INT")
    private Integer currentParticipants;

    @NotNull(message = "Game cannot be null")
    @Column(columnDefinition = "VARCHAR(50)") //some games have really long names
    @Size(max = 50, min = 4, message = "Game must have between 4 to 50 characters")
    private String game;

    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    @Size(min=4,max = 10,
            message = "Organizer name must have between 4 to 10 characters")
    private String organizerName;

    //RELATIONS

    //Rafeef
    @ManyToOne
    @JsonIgnore
    private Organizer organizer;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Participant> participants;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Match> matches;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Round> rounds;
}
