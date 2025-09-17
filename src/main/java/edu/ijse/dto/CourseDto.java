package edu.ijse.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDto {
    private int courseID;
    private String courseName;
    private String duration;
    private BigDecimal fee;
    private List<StudentDto> studentList;
    private List<InstructorDto> instructors;

    public CourseDto(int courseID, String courseName, String duration, BigDecimal fee) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.duration = duration;
        this.fee = fee;
    }

    public CourseDto(int courseID, String courseName, String duration, BigDecimal fee, List<InstructorDto> instructors) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.duration = duration;
        this.fee = fee;
        this.instructors = instructors;
    }
}
