package edu.ijse.tm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonsTM {
    private int lessonID;
    private String lessonName;

    private int studentID;       // add this
    private String studentName;

    private int instructorID;    // add this
    private String instructorName;

    private int courseID;        // add this
    private String courseName;

    private LocalDate lessonDate;
    private String lessonStartTime;
    private String lessonEndTime; // Stored as String for display
}
