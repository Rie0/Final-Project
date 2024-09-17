package org.twspring.noob.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MultiInviteDTO { //Rafeef

    @NotEmpty(message = "Player usernames cannot be empty")
    private List<String> playerUsernames;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Message is required")
    private String message;
}
