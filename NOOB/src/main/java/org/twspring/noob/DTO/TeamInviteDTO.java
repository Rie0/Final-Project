package org.twspring.noob.DTO;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamInviteDTO { //rafeef

    @NotEmpty(message = "Title cannot be empty")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    //is set in the service
    private String teamName;

    @NotEmpty(message = "Message cannot be empty")
    @NotBlank(message = "Message cannot be blank")
    private String message;
}
