package org.twspring.noob.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Set;
//Hussam

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bracket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generate IDs
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament; // Changed from OneToOne to ManyToOne

    @OneToMany(mappedBy = "bracket", cascade = CascadeType.ALL)
    private Set<Round> rounds;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String bracketType;
}
