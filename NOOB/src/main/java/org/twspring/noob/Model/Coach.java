package org.twspring.noob.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coach {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer coachId;

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


}
