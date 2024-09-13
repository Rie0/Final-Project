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

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String type; // Type of tournament (e.g., "SINGLE_ELIMINATION", "DOUBLE_ELIMINATION")

    @Column(columnDefinition = "Date")
    private LocalDate startDate;

    @Column(columnDefinition = "Date")
    private LocalDate endDate;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String status; // Status of the tournament (e.g., "PENDING", "ONGOING", "COMPLETED")

    @Column(columnDefinition = "VARCHAR(255)")
    private String location;

    @NotNull(message = "Maximum participants must not be null")
    @Min(value = 1, message = "Maximum participants must be at least 1")
    @Column(columnDefinition = "INT NOT NULL")
    private Integer maxParticipants;

    @Min(value = 0, message = "Current participants cannot be negative")
    @Column(columnDefinition = "INT")
    private Integer currentParticipants;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String game; // Game associated with the tournament (e.g., "Chess", "Soccer")

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String city; // City where the tournament is held

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String attendanceType; // Attendance type (e.g., "Online", "Onsite")

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Participant> participants;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Match> matches;

    @OneToOne(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Bracket bracket;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer; // Reference to the Organizer creating the tournament
}
