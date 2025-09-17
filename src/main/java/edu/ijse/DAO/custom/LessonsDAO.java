package edu.ijse.DAO.custom;

import edu.ijse.DAO.CrudDAO;
import edu.ijse.entity.Lessons;

import java.time.LocalDate;
import java.time.LocalTime;


public interface LessonsDAO extends CrudDAO<Lessons> {
    boolean checkLessonOverlapsByInstructorID(int instructorId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
