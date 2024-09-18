package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    @Size(min = 2, max = 100, message = "Name must have between 2 to 100 characters")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;



    @FutureOrPresent(message = "Date must be in the future or today")
    @Column(columnDefinition = "Date")
    private LocalDate startDate;

    @Column(columnDefinition = "Date")
    @Future(message = "Date must be in the future or today")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null")
    private Status status= Status.OPEN;

    public enum Status{
        OPEN,
        ACTIVE,
        FULL,
        FINISHED
    } // Status of the tournament

    @Column(columnDefinition = "VARCHAR(255)")
    private String location;

    @NotNull(message = "Maximum participants must not be null")
    @Min(value = 1, message = "Maximum participants must be at least 1")
    @Column(columnDefinition = "INT NOT NULL")
    private Integer maxParticipants;

    @Column(columnDefinition = "INT")
    private Integer currentParticipants = 0;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String game; // Game associated with the tournament (e.g., "Chess", "Soccer")

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String type; // teams or players

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")

    private String city; // City where the tournament is held

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String attendanceType; // Attendance type (e.g., "Online", "Onsite")


    @Column(columnDefinition = "VARCHAR(100) ")
    @Size(min = 2, max = 100, message = "Permit name must have between 2 to 100 characters")
    private String permit; // Name of the Permit

    @Column(columnDefinition = "INT")
    @Positive(message = "Prize pool must be a positive number")
    private int prizePool;


    @Column(columnDefinition = "INT")
    @Positive(message = "ParticipantsPrize must be a positive number")
    private int participantsPrize;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Participant> participants;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Match> matches;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Bracket> brackets;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer; // Reference to the Organizer creating the tournament
}
