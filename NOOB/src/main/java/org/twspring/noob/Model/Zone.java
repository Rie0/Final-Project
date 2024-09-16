package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Zone Type   can not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String zoneType;
    @NotNull(message = "Zone Capacity   can not be empty")
    @Column(columnDefinition = "int not null")
    private int zoneCapacity;
    private boolean isAvailable;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "zone")
    private Set<PC> pc;


    @ManyToOne
    @JsonIgnore
    private PcCentres pcCentre;


    @ManyToOne
    @JsonIgnore
    private Subscription subscription;



}
