package org.twspring.noob.Model;

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
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null")
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name should not exceed 100 characters")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Size(max = 500, message = "Bio should not exceed 500 characters")
    private String bio;

    @Column(columnDefinition = "varchar(100) not null")
    @NotBlank(message = "Expertise is mandatory")
    @Size(max = 100, message = "Expertise should not exceed 100 characters")
    private String expertise;

    @Min(value = 0, message = "Rating should not be less than 0")
    @Max(value = 5, message = "Rating should not be more than 5")
    @Column(columnDefinition = "varchar(30) not null")
    private double rating = 0;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<MasterClass> masterClasses;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<CoachingSession> coachingSessions;

    @OneToOne
    @MapsId
    private User user;
}