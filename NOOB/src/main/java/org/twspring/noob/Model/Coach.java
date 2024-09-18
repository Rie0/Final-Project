package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// Mohammed
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name should not exceed 100 characters")
    @Column(columnDefinition = "varchar(75) not null")
    private String name;


    @Size(max = 500, message = "Bio should not exceed 500 characters")
    @Column(columnDefinition = "varchar(150) not null")
    private String bio;

    @NotBlank(message = "Expertise is mandatory")
    @Size(max = 100, message = "Expertise should not exceed 100 characters")
    @Column(columnDefinition = "varchar(125) not null")
    private String expertise;


    @NotNull(message = "hourlyRate is mandatory")
    @Positive(message = "hourlyRate should be positive")
    @Column(columnDefinition = "varchar(25) not null")
    private Integer hourlyRate;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<MasterClass> masterClasses;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<CoachingSession> coachingSessions;


    @OneToOne
    @MapsId
    @JsonIgnore

    private User user;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<Review> reviews;
}