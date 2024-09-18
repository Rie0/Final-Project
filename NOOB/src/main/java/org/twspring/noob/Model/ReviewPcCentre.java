package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
////Hassan Alzahrani
public class ReviewPcCentre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull(message = "rating can not be empty")
    @Column(columnDefinition = "int not null")
    @Size(min = 1,max = 5)
    private int rating;

    @NotEmpty(message = "comment can not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String commnet;

    @ManyToOne
    @JsonIgnore
    private Player player;

    @ManyToOne
    @JsonIgnore
    private PcCentres pcCentre;



}
