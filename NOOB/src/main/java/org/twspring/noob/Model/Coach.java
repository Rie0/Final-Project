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

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name should not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Bio should not exceed 500 characters")
    private String bio;

    @NotBlank(message = "Expertise is mandatory")
    @Size(max = 100, message = "Expertise should not exceed 100 characters")
    private String expertise;

    @NotNull(message = "Rating is mandatory")
    @Min(value = 0, message = "Rating should not be less than 0")
    @Max(value = 5, message = "Rating should not be more than 5")
    private Float rating;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<MasterClass> masterClasses;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<CoachingSession> coachingSessions;
}