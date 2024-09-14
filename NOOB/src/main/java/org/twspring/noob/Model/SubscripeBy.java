package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertFalse;
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
    @AssertFalse
    private boolean status;
    private int remainingHours;


    @ManyToOne
    @JsonIgnore
    private Subscription subscription;


}
