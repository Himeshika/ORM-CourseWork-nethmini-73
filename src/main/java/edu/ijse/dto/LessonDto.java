package edu.ijse.dto;

import edu.ijse.entity.Course;
import edu.ijse.entity.Instructor;
import edu.ijse.entity.Student;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LessonDto {
    private int lessonId;
    private String lessonName;
    private LocalDate lessonDate;
    private LocalTime lessonStartTime;
    private LocalTime lessonEndTime;
    private Course course;
    private Instructor instructor;
    private Student student;
}
