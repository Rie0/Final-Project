package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @JsonIgnore
    private Coach coach;

    @NotNull(message = "Date is mandatory")
    private LocalDate date;

    @NotNull(message = "Start time is mandatory")
    private LocalTime startTime;

    @NotNull(message = "End time is mandatory")
    private LocalTime endTime;

    @NotNull(message = "Booking status is mandatory")
    private Boolean isBooked = false;




//    @ManyToOne
//    @JoinColumn(name = "master_class_id")
//    private MasterClass masterClass;

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL)
    @JsonIgnore
    private CoachingSession coachingSession;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;



}