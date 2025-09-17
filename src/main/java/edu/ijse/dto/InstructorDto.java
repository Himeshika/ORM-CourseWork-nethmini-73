package edu.ijse.dto;

import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class InstructorDto {
    private int instructorID;
    private String instructorName;
    private String instructorEmail;
    private String instructorAvailability;
    private List<LessonDto> lessons;

    public InstructorDto(int instructorID, String instructorName, String instructorEmail, String instructorAvailability) {
        this.instructorID = instructorID;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.instructorAvailability = instructorAvailability;
    }
}
