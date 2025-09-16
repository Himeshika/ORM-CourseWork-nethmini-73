package edu.ijse.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users") //was user
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String username;
    private String password; // i will use bcrypt here
    private String role;
    private String email;
    private boolean active;
}
