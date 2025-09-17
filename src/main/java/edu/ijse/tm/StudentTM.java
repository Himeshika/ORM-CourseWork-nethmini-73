package edu.ijse.tm;

import edu.ijse.dto.CourseDto;
import edu.ijse.dto.LessonDto;
import edu.ijse.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentTM {
    private int studentID;
    private String studentName;
    private String studentEmail;
    private String contactNO;
    private String address;
    private LocalDate dateOfBirth;
    private LocalDate registrationDate;
    private int progress;
    private List<CourseDto> courses;
    private List<LessonDto> lessons;
    private List<PaymentDto> payments;
}
