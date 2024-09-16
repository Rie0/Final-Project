package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Future;
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
public class SubscripeBy {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

private Date startDate;
    private Date endDate;

    private boolean status;
    private int remainingHours;


    @ManyToOne
    @JsonIgnore
    private Subscription subscription;

    @ManyToOne
    @JsonIgnore
    private Player player;

}
