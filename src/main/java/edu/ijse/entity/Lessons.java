package edu.ijse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Lessons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lessonId;
    private String lessonName;
    private LocalDate lessonDate;
    private LocalTime lessonStartTime;
    private LocalTime lessonEndTime;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="course_id")
    private Course course;
    @ManyToOne
    @JoinColumn(name="instructor_id")
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    public Lessons(int lessonId, String lessonName, LocalDate lessonDate, LocalTime lessonStartTime, LocalTime lessonEndTime, Course course, Instructor instructor) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.lessonDate = lessonDate;
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
        this.course = course;
        this.instructor = instructor;
    }
}
