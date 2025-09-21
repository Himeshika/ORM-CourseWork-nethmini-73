package edu.ijse.BO.custom;

import edu.ijse.BO.SuperBO;
import edu.ijse.dto.InstructorDto;

import java.util.List;

public interface InstructorBO extends SuperBO {
    boolean saveInstructor(InstructorDto dto);
    boolean updateInstructor(InstructorDto dto);
    boolean deleteInstructor(int id);
    List<InstructorDto> getAllInstructors();
}
