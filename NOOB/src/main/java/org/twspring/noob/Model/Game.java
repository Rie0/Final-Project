package org.twspring.noob.Model;

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
public class Game {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Game Name  can not be empty")
   @Column(columnDefinition = "varchar(30) not null")
    private String gameName;
    @NotEmpty(message = "Game Genres  can not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String gameGenres;

    @ManyToMany(mappedBy = "game")
    private Set<PC>pc;

}
