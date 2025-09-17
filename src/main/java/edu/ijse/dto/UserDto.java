package edu.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private int userId;
    private String username;
    private String password; // i will use bcrypt here
    private String role;
    private String email;
    private boolean active;
}
