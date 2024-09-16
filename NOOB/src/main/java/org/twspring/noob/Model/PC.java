package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PC {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Brand Name  can not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String brand;
    @NotEmpty(message = "model Name  can not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String model;
    @NotEmpty(message = "specs Name  can not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String specs;
    private boolean isAvailable;



    @ManyToMany
    @JsonIgnore
    private Set<Game>game;

    @ManyToOne
    @JsonIgnore
    private PcCentres pcCentre;


    @ManyToOne
    @JsonIgnore
    private Zone zone;






}
