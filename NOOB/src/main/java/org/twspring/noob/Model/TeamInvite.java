package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamInvite { //rafeef

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(25)")
    private String title;

    @Column(columnDefinition = "VARCHAR(25)")
    private String teamName;

    @Column(columnDefinition = "VARCHAR(500)")
    private String message;

    @Enumerated(EnumType.STRING)
    private Status status= Status.PENDING;

    public enum Status{
        PENDING,
        APPROVED,
        DECLINED
    }

    //RELATIONS
    @ManyToOne
    @JsonIgnore
    private Team team;

    @ManyToOne
    @JsonIgnore
    private Player player;

}
