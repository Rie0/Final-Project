package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Subscription Tie   can not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String subscriptionTie;
    @NotNull(message = "price can not be empty")
    @Column(columnDefinition = "int not null")
    private double price;
    @NotNull(message = "Subscription Hours can not be empty")
    @Column(columnDefinition = "int not null")
    private int subscriptionHours;
    @NotNull(message = "members can not be empty")
    @Column(columnDefinition = "int not null")
private int members;
private Date subscriptionDate;



    @OneToMany(cascade = CascadeType.ALL,mappedBy = "subscription")
    private Set<SubscripeBy>subscribeBy;

    @ManyToOne
    @JsonIgnore
    private PcCentres pcCentres;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "subscription")
    private Set<Zone> zones;

}
