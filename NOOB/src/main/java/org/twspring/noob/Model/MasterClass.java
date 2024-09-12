package org.twspring.noob.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MasterClass {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer masterClassId;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @NotNull(message = "Coach is mandatory")
    private Coach coach;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    @NotNull(message = "Start date is mandatory")
    private LocalDateTime startDate;

    @NotNull(message = "End date is mandatory")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "masterClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoachingSession> sessions;

    @NotBlank(message = "Status is mandatory")
    @Size(max = 50, message = "Status should not exceed 50 characters")
    private String status;

}
