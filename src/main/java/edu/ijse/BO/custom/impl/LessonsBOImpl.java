package edu.ijse.BO.custom.impl;

import edu.ijse.BO.custom.LessonsBO;
import edu.ijse.DAO.DAOFactory;
import edu.ijse.DAO.custom.CourseDAO;
import edu.ijse.DAO.custom.InstructorDAO;
import edu.ijse.DAO.custom.LessonsDAO;
import edu.ijse.DAO.custom.StudentDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.dto.LessonDto;
import edu.ijse.entity.*;
import edu.ijse.exception.*;
import edu.ijse.exception.IllegalArgumentException;
import edu.ijse.util.RegExChecker;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class LessonsBOImpl implements LessonsBO {

    private StudentDAO studentDAO=(StudentDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.STUDENT);
    private InstructorDAO instructorDAO=(InstructorDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.INSTRUCTOR);
    private CourseDAO courseDAO=(CourseDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.COURSE);
    private LessonsDAO lessonsDAO=(LessonsDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.LESSONS);
    @Override
    public boolean saveLesson(int studentID, int instructorID,int courseID, LessonDto lessonDto) {// save lesson tx

        if(!RegExChecker.isLessonNameValid(lessonDto.getLessonName())){
            throw new IllegalArgumentException("invalid lesson name");
        }
        Session session= FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx= session.beginTransaction();
        try {

                Student student=studentDAO.findById(studentID,session);//1-fetching student
                if (student == null) {
                    throw new NotFoundException("Student not found with ID: " + studentID);
                }
                Instructor instructor=instructorDAO.findById(instructorID,session);//2-fetch instruc.. by id, heeeee : )
                if (instructor == null) {
                    throw new NotFoundException("Instructor not found with ID: " + instructorID);
                }
                if(!instructor.getAvailability().equals(Availability_instructor.AVAILABLE)){
                    throw new InvalidInstructorException("Instructor is not available for this lesson");//3-check for instructor availability
                }
                 Course course=courseDAO.findById(courseID,session); //fetch the course
                 if (course == null) {
                    throw new NotFoundException("Course not found with ID: " + courseID);
                 }

                 if(!instructor.getCourses().contains(course)){
                      throw new InvalidInstructorException("Instructor is not available for this course");
                 }

                if (lessonsDAO.checkLessonOverlapsByInstructorID(instructorID,lessonDto.getLessonDate(),lessonDto.getLessonStartTime(),lessonDto.getLessonEndTime())) {
                     throw new LessonConflictException("Lesson is already overlapping for this lesson");   //check for lesson overlaps
                }


                Lessons lessons=new Lessons();
                lessons.setLessonName(lessonDto.getLessonName());
                lessons.setLessonDate(lessonDto.getLessonDate());
                lessons.setLessonStartTime(lessonDto.getLessonStartTime());
                lessons.setLessonEndTime(lessonDto.getLessonEndTime());
                lessons.setInstructor(instructor);
                lessons.setCourse(course);
                lessons.setStudent(student);

                boolean lessonSaved=lessonsDAO.save(lessons,session);
                if (!lessonSaved){
                    throw  new LessonAllocationException("Lessons could not be saved");
                }
                int currentProgress=student.getProgress();
                student.setProgress(currentProgress+1);
                tx.commit();

                return lessonSaved;

        }catch (Exception e){
            tx.rollback();
            throw e;

        }
    }

    @Override
    public boolean updateLesson(int studentID, int instructorID,int courseID, LessonDto dto) {
        if (!RegExChecker.isLessonNameValid(dto.getLessonName())) {
            throw new IllegalArgumentException("Invalid lesson name");
        }

        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();

        try {

            Lessons existingLesson = lessonsDAO.findById(dto.getLessonId(), session);
            if (existingLesson == null) {
                throw new NotFoundException("Lesson not found with ID: " + dto.getLessonId());
            }


            Student student = studentDAO.findById(studentID, session);
            if (student == null) throw new NotFoundException("Student not found with ID: " + studentID);

            Instructor instructor = instructorDAO.findById(instructorID, session);
            if (instructor == null) throw new NotFoundException("Instructor not found with ID: " + instructorID);

            Course course = courseDAO.findById(courseID, session);
            if (course == null) throw new NotFoundException("Course not found with ID: " + courseID);


            if (!instructor.getAvailability().equals(Availability_instructor.AVAILABLE)) {
                throw new InvalidInstructorException("Instructor is not available for this lesson");
            }

            if (!instructor.getCourses().contains(course)) {
                throw new InvalidInstructorException("Instructor is not assigned to this course");
            }


            if (lessonsDAO.checkLessonOverlapsByInstructorID(
                    instructor.getInstructorID(),
                    dto.getLessonDate(),
                    dto.getLessonStartTime(),
                    dto.getLessonEndTime())
            ) {
                throw new LessonConflictException("Lesson overlaps with another lesson for this instructor");
            }


            existingLesson.setLessonName(dto.getLessonName());
            existingLesson.setLessonDate(dto.getLessonDate());
            existingLesson.setLessonStartTime(dto.getLessonStartTime());
            existingLesson.setLessonEndTime(dto.getLessonEndTime());
            existingLesson.setStudent(student);
            existingLesson.setInstructor(instructor);
            existingLesson.setCourse(course);

            boolean updated = lessonsDAO.update(existingLesson, session);
            tx.commit();
            return updated;

        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean deleteLesson(int id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            Lessons lesson = lessonsDAO.findById(id, session);
            if (lesson == null) {
                throw new NotFoundException("Lesson not found with ID: " + id);
            }

            Student student = lesson.getStudent();
            if (student != null) {
                int currentProgress = student.getProgress();
                if (currentProgress > 0) {
                    student.setProgress(currentProgress - 1);
                }
            }

            boolean deleted = lessonsDAO.delete(id);
            tx.commit();
            return deleted;

        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public List<LessonDto> getAllLessons() {
        List<LessonDto> dtoList = new ArrayList<>();
        List<Lessons> lessonsList = lessonsDAO.getAll();

        for (Lessons lesson : lessonsList) {
            dtoList.add(LessonDto.builder()
                    .lessonId(lesson.getLessonId())
                    .lessonName(lesson.getLessonName())
                    .lessonDate(lesson.getLessonDate())
                    .lessonStartTime(lesson.getLessonStartTime())
                    .lessonEndTime(lesson.getLessonEndTime())
                    .student(lesson.getStudent())
                    .instructor(lesson.getInstructor())
                    .course(lesson.getCourse())
                    .build());
        }

        return dtoList;
    }
    }

