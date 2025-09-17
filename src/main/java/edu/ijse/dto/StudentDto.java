package edu.ijse.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StudentDto {
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
    private List<PaymentDto>  payments;

    public StudentDto(int studentID, String studentName, String studentEmail, String contactNO, String address, LocalDate dateOfBirth, LocalDate registrationDate, int progress) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.contactNO = contactNO;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.registrationDate = registrationDate;
        this.progress = progress;
    }
}
