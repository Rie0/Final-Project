package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotEmpty;
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
public class PcCentres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @NotEmpty(message = "centre name can not be empty")
//    @Column(columnDefinition = "int not null")
    private String centreName;

//    @NotEmpty(message = "location    can not be empty")
//    @Column(columnDefinition = "int not null")
    private String location;
//
//    @NotEmpty(message = "opening Hours can not be empty")
//    @Column(columnDefinition = "int not null")
    private String openingHours;

//    @NotEmpty(message = "number of PC Capacity can not be empty")
//    @Column(columnDefinition = "int not null")
    private int numberOfPc;
//    @NotEmpty(message = "Contact Number can not be empty")
//    @Column(columnDefinition = "varchar(10) not null")
//    @Pattern(regexp = "/^(009665|9665|\\+9665|05|5)(5|0|3|6|4|9|1|8|7)([0-9]{7})$/")
    private String contactNumber;
    @AssertFalse
    private boolean approved;
//    @NotEmpty(message = "rating can not be empty")
//    @Column(columnDefinition = "int not null")
    private int rating;



    @OneToMany(cascade = CascadeType.ALL,mappedBy = "pcCentre")
    private Set<PC>pc;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "pcCentre")
    private Set<Zone> zone;

    @ManyToOne
    @JsonIgnore
    private Vendor vendor;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "pcCentres")
    private Set<Subscription>subscriptions;

}
