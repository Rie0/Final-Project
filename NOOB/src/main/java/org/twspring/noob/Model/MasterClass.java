package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MasterClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @NotNull(message = "Coach is mandatory")
    @JsonIgnore
    private Coach coach;

//    @OneToMany(mappedBy = "masterClass", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private Set<Schedule> schedules;

    @ManyToMany
    @JoinTable(
            name = "masterclass_player",
            joinColumns = @JoinColumn(name = "masterclass_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private Set<Player> players = new HashSet<>();

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    @NotNull(message = "Start date is mandatory")
    private LocalDate startDate;

    @NotNull(message = "End date is mandatory")
    private LocalDate endDate;

    @NotNull(message = "Start time is mandatory")
    private LocalTime startTime;

    @NotNull(message = "End time is mandatory")
    private LocalTime endTime;

    @NotBlank(message = "Status is mandatory")
    @Size(max = 50, message = "Status should not exceed 50 characters")
    private String status;
}