package edu.ijse.tm;

import edu.ijse.dto.InstructorDto;
import edu.ijse.dto.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseTM {
    private int courseID;
    private String courseName;
    private String duration;
    private BigDecimal fee;
    private List<StudentDto> studentList;
    private List<InstructorDto> instructorList;

    public String getInstructors() {
        if (instructorList == null || instructorList.isEmpty()) return "";
        return instructorList.stream()
                .map(InstructorDto::getInstructorName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

}
