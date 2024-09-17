package org.twspring.noob.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProfileDTO {

    private String username;
    private String bio;

    private LocalDateTime joinDate;
}
