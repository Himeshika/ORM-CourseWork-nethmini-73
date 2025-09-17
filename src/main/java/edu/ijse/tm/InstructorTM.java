package edu.ijse.tm;

import edu.ijse.dto.LessonDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorTM {
    private int instructorID;
    private String instructorName;
    private String instructorEmail;
    private String availability;
    private List<LessonDto> lessons;
}
