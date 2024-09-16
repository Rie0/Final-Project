package org.twspring.noob.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
public class Vendor {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Venue Name    can not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String VenueName;


    @OneToOne
    @MapsId
    private User user;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vendor")
    private Set<PcCentres> pcCentres;


}
