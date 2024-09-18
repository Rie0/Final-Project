package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
////Hassan Alzahrani
public class SubscripeBy {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

private Date startDate;
    private Date endDate;

    @NotNull(message = "Subscription Hours can not be empty")
    @Column(columnDefinition = "int not null")
private int playerMembers;
    private boolean status=false;
    @NotNull(message = "Subscription Hours can not be empty")
    @Column(columnDefinition = "int not null")
    private int remainingHours;

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;


    @ManyToOne
    @JsonIgnore
    private Subscription subscription;

    @ManyToOne
    @JsonIgnore
    private Player player;

}
