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
public class Organizer {

    @Id
    private Integer id; // Unique identifier for the organizer

    @NotEmpty(message = "Name cannot be empty")
    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    @Size(min = 2, max = 100, message = "Name must have between 2 to 100 characters")
    private String name; // Name of the organizer

    @NotEmpty(message = "Contact information cannot be empty")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String contactInfo; // Contact information (email, phone, etc.)

    @NotEmpty(message = "Organization name cannot be empty")
    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    @Size(min = 2, max = 100, message = "Organization name must have between 2 to 100 characters")
    private String organizationName; // Name of the organization



    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tournament> tournaments; // List of tournaments created by the organizer

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<League> leagues; // List of leagues created by the organizer

    //rafeef
    @OneToOne
    @MapsId
    private User user;
}

