package edu.ijse.BO.custom;

import edu.ijse.BO.SuperBO;
import edu.ijse.dto.LessonDto;

import java.util.List;

public interface LessonsBO extends SuperBO {
    boolean saveLesson(int  studentID, int instructorID,int CourseID, LessonDto lessonDto);
    boolean updateLesson(int studentID, int instructorID,int courseID, LessonDto dto);
    boolean deleteLesson(int id);
    List<LessonDto> getAllLessons();
}
