package edu.ijse.BO.custom;

import edu.ijse.BO.SuperBO;
import edu.ijse.dto.CourseDto;
import edu.ijse.dto.InstructorDto;

import java.util.List;

public interface CourseBO extends SuperBO {
    boolean saveCourse(CourseDto course,List<InstructorDto> instructorDtos);
    boolean updateCourse(CourseDto course);
    boolean deleteCourse(int id);
    List<CourseDto> getAllCourses();
}
