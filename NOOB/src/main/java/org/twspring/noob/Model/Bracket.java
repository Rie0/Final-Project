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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bracket {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Tournament tournament;

    @OneToMany(mappedBy = "bracket", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Round> rounds;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String bracketType;
}
