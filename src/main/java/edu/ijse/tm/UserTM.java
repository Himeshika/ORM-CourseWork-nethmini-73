package edu.ijse.tm;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTM {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String email;
    private boolean active;
}
