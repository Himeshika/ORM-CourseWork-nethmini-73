package edu.ijse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="instructor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int instructorID;
    private String instructorName;
    private String instructorEmail;
    @Enumerated(EnumType.STRING)
    private Availability_instructor availability;
    @OneToMany(mappedBy ="instructor",cascade = CascadeType.ALL)
    private List<Lessons> lessons;
    @ManyToMany(mappedBy = "instructorList")
    private List<Course> courses;


    public Instructor(int instructorID, String instructorName, String instructorEmail) {
        this.instructorID = instructorID;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
    }
}
