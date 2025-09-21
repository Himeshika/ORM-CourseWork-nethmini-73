package edu.ijse.BO.custom.impl;

import edu.ijse.BO.custom.CourseBO;
import edu.ijse.DAO.DAOFactory;
import edu.ijse.DAO.custom.CourseDAO;
import edu.ijse.DAO.custom.InstructorDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.dto.CourseDto;
import edu.ijse.dto.InstructorDto;
import edu.ijse.entity.Course;
import edu.ijse.entity.Instructor;
import edu.ijse.exception.DuplicateEntryException;
import edu.ijse.util.RegEXUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseBOImpl implements CourseBO {

    private final CourseDAO courseDAO = (CourseDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.COURSE);
    private InstructorDAO instructorDAO=(InstructorDAO)DAOFactory.getDAO(DAOFactory.DAOTypes.INSTRUCTOR);

    @Override
    public boolean saveCourse(CourseDto dto, List<InstructorDto> instructorDtos) {

        if (!RegEXUtil.isValidCourseName(dto.getCourseName())) {
            throw new IllegalArgumentException("Invalid course name!");
        }

        if (!RegEXUtil.isValidDuration(dto.getDuration())) {
            throw new IllegalArgumentException("Invalid duration format!");
        }

        if (dto.getFee() == null || dto.getFee().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Fee must be greater than 0");
        }
        Session session= FactoryConfiguration.getInstance().getSession();
        Transaction tx= session.beginTransaction();
        try {
            assert courseDAO != null;
            boolean courseExist=courseDAO.getCourseByName(dto.getCourseName(),session);
            if(courseExist){
                throw new DuplicateEntryException("Course already exist");
            }
            Course course=new Course();
            course.setCourseName(dto.getCourseName());
            course.setDuration(dto.getDuration());
            course.setFee(dto.getFee());

            List<Instructor> instructorList=new ArrayList<>();
            for(InstructorDto instructorDto:instructorDtos){
                Instructor instructor=instructorDAO.findINsById(instructorDto.getInstructorID(),session);
                if(instructor!=null){
                    instructorList.add(instructor);
                    instructor.getCourses().add(course);
                }
            }
            course.setInstructorList(instructorList);
            assert courseDAO != null;
            boolean courseSaved=courseDAO.save(course,session);
            tx.commit();
            return courseSaved;

        }catch(Exception e){
            tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean updateCourse(CourseDto dto) {
        if (!RegEXUtil.isValidCourseName(dto.getCourseName())) {
            throw new IllegalArgumentException("Invalid course name!");
        }

        if (!RegEXUtil.isValidDuration(dto.getDuration())) {
            throw new IllegalArgumentException("Invalid duration format!");
        }

        if (dto.getFee() == null || dto.getFee().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Fee must be greater than 0");
        }

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {

            Course existingCourse = courseDAO.findById(dto.getCourseID(), session);
            if (existingCourse == null) {
                throw new IllegalArgumentException("Course not found with ID: " + dto.getCourseID());
            }


            boolean duplicateExists = courseDAO.getCourseByName(dto.getCourseName(), session);
            if (duplicateExists && !existingCourse.getCourseName().equals(dto.getCourseName())) {
                throw new DuplicateEntryException("Another course already exists with the same name");
            }


            existingCourse.setCourseName(dto.getCourseName());
            existingCourse.setDuration(dto.getDuration());
            existingCourse.setFee(dto.getFee());


            List<Instructor> instructorList = new ArrayList<>();
            if (dto.getInstructors() != null) {
                for (InstructorDto instructorDto : dto.getInstructors()) {
                    Instructor instructor = instructorDAO.findINsById(instructorDto.getInstructorID(), session);
                    if (instructor != null) {
                        instructorList.add(instructor);
                        instructor.getCourses().add(existingCourse);
                    }
                }
            }
            existingCourse.setInstructorList(instructorList);


            boolean updated = courseDAO.update(existingCourse, session);
            tx.commit();
            return updated;

        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }


    @Override
    public boolean deleteCourse(int id) {
        return courseDAO.delete(id);
    }


    @Override
    public List<CourseDto> getAllCourses() {
        List<Course> courseList = courseDAO.getAll();
        List<CourseDto> dtoList = new ArrayList<>();

        for (Course c : courseList) {

            List<InstructorDto> instructorDtos = c.getInstructorList().stream()
                    .map(i -> new InstructorDto(
                            i.getInstructorID(),
                            i.getInstructorName(),
                            i.getInstructorEmail(),
                            i.getAvailability().toString()
                    ))
                    .collect(Collectors.toList());

            dtoList.add(new CourseDto(
                    c.getCourseID(),
                    c.getCourseName(),
                    c.getDuration(),
                    c.getFee(),
                    instructorDtos
            ));
        }
        return dtoList;
    }

    @Override
    public List<Object[]> loadModulePieChart() {
        return courseDAO.getStudentCountPerCourse();
    }

}