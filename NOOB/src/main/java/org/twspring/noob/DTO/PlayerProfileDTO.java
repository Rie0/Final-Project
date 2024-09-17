package org.twspring.noob.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PlayerProfileDTO { //rafeef

    private String username;

    private Integer age;

    private String bio;

    private String teamName;

    private LocalDateTime joinDate;
}
