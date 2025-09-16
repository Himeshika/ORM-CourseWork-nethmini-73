package edu.ijse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentID;
    private String studentName;
    private String studentEmail;
    private String contactNO;
    private String address;
    private LocalDate dateOfBirth;
    private LocalDate registrationDate;
    private int progress;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )//this is many to many with student ,hehe
    private List<Course> courses;
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<Lessons> lessons;
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Payment>  payments;

    public Student(int studentID, String studentName, String studentEmail, String contactNO, String address, LocalDate dateOfBirth, LocalDate registrationDate, int progress) {
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
