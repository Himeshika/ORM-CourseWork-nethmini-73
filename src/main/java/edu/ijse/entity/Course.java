package edu.ijse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseID;
    private String courseName;
    private String duration;
    private BigDecimal fee;
    @ManyToMany(mappedBy = "courses")
    private List<Student> studentList;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lessons> lessonsList;
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_instructor", joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id"))//this is mny to many with instructor
    private List<Instructor> instructorList;

    public Course(int courseID, String courseName, String duration, BigDecimal fee) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.duration = duration;
        this.fee = fee;
    }
}
