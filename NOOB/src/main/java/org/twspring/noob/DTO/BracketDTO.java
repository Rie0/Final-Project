package org.twspring.noob.DTO;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.twspring.noob.Model.Round;
import org.twspring.noob.Model.Tournament;

import java.util.List;

//Hussam

@Data
@AllArgsConstructor
public class BracketDTO {


    @NotNull(message = "Tournament ID cannot be null")
    private Integer tournamentId;

    @NotEmpty(message = "List of round IDs cannot be empty")
    private List<Integer> roundIds;

    @NotEmpty(message = "Bracket type cannot be empty")
    private String bracketType;
}
