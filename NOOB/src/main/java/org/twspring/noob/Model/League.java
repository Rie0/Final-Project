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
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    @Size(min = 2, max = 100, message = "Name must have between 2 to 100 characters")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

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


    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Participant> participants;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Match> matches;

    @ManyToOne
    private Organizer organizer; // Reference to the Organizer creating the tournament
}
