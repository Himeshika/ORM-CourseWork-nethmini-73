package edu.ijse.BO.custom.impl;

import edu.ijse.BO.custom.StudentBO;
import edu.ijse.DAO.DAOFactory;
import edu.ijse.DAO.custom.CourseDAO;
import edu.ijse.DAO.custom.PaymentDAO;
import edu.ijse.DAO.custom.StudentDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.dto.CourseDto;
import edu.ijse.dto.PaymentDto;
import edu.ijse.dto.StudentDto;
import edu.ijse.entity.Course;
import edu.ijse.entity.Payment;
import edu.ijse.entity.Student;
import edu.ijse.exception.DuplicateEntryException;
import edu.ijse.exception.NotFoundException;
import edu.ijse.util.RegExChecker;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentBOImpl implements StudentBO {

    StudentDAO studentDAO=(StudentDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.STUDENT);
    CourseDAO courseDAO=(CourseDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.COURSE);
    PaymentDAO paymentDAO=(PaymentDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.PAYMENT);

    @Override
    public boolean saveStudent(StudentDto student, List<CourseDto> courseDtos, PaymentDto paymentDto) {// save student tx

        if(!RegExChecker.isValidName(student.getStudentName())){
            throw new IllegalArgumentException("invalid student name");
        }
        if (!RegExChecker.isValidEmail(student.getStudentEmail())) {
            throw new IllegalArgumentException("Invalid student email");
        }
        if (!RegExChecker.isValidAddress(student.getAddress())) {
            throw new IllegalArgumentException("Invalid student address");
        }
        if (!RegExChecker.isValidPhone(student.getContactNO())) {
            throw new IllegalArgumentException("Invalid contact number");
        }
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            boolean exist=studentDAO.checkStudent(student.getStudentEmail(),session);
             if(exist){
                throw new DuplicateEntryException("student already exist");
             }

             Student nStudent=new Student();
             nStudent.setStudentName(student.getStudentName());
             nStudent.setStudentEmail(student.getStudentEmail());
             nStudent.setContactNO(student.getContactNO());
             nStudent.setAddress(student.getAddress());
             nStudent.setRegistrationDate(LocalDate.now());
             nStudent.setDateOfBirth(student.getDateOfBirth());

             List<Course> courseList=new ArrayList<>();
            for (CourseDto cDto : courseDtos) {
                Course course = courseDAO.getCourseById(cDto.getCourseID(),session);
                if (course != null) {
                    courseList.add(course);

                    if(course.getStudentList()!=null){
                        course.getStudentList().add(nStudent);
                    }
                }
            }
             nStudent.setCourses(courseList);

            if (paymentDto.getPaymentAmount() == null || paymentDto.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Payment amount must be greater than zero");
            }
            if (paymentDto.getPaymentType() == null || paymentDto.getPaymentType().isBlank()) {
                throw new IllegalArgumentException("Payment type cannot be empty");
            }

            Payment payment = new Payment();
            payment.setPaymentDate(paymentDto.getPaymentDate());
            payment.setPaymentType(paymentDto.getPaymentType());
            payment.setPaymentAmount(paymentDto.getPaymentAmount());
            payment.setStatus(paymentDto.getStatus());
            payment.setStudent(nStudent);

            nStudent.setPayments(new ArrayList<>(List.of(payment)));

             boolean studentSaveDone=studentDAO.save(nStudent,session);
             transaction.commit();
             return studentSaveDone;

        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public ArrayList<StudentDto> loadAllStudent() {
        List<Student> students = studentDAO.getAll();
        ArrayList<StudentDto> dtoList = new ArrayList<>();

        for (Student s : students) {
            StudentDto dto = new StudentDto(
                    s.getStudentID(),
                    s.getStudentName(),
                    s.getStudentEmail(),
                    s.getContactNO(),
                    s.getAddress(),
                    s.getDateOfBirth(),
                    s.getRegistrationDate(),
                    s.getProgress(),
                    mapCourses(s.getCourses()),
                    null,
                    mapPayments(s.getPayments())
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    private List<CourseDto> mapCourses(List<Course> courses) {
        if (courses == null) return new ArrayList<>();
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course c : courses) {
            courseDtos.add(new CourseDto(
                    c.getCourseID(),
                    c.getCourseName(),
                    c.getDuration(),
                    c.getFee()
            ));
        }
        return courseDtos;
    }

    private List<PaymentDto> mapPayments(List<Payment> payments) {
        if (payments == null) return new ArrayList<>();
        List<PaymentDto> paymentDtos = new ArrayList<>();
        for (Payment p : payments) {
            paymentDtos.add(new PaymentDto(
                    p.getPaymentId(),
                    p.getPaymentDate(),
                    p.getPaymentType(),
                    p.getPaymentAmount(),
                    p.getStatus(),
                    p.getStudent()
            ));
        }
        return paymentDtos;
    }

    @Override
    public boolean updateStudent(StudentDto studentDto, List<CourseDto> courseDtos, PaymentDto paymentDto) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {

            Student studentEty = studentDAO.findById(studentDto.getStudentID(),session);
            if (studentEty == null) {
                throw new NotFoundException("Student not found");
            }


            studentEty.setStudentName(studentDto.getStudentName());
            studentEty.setStudentEmail(studentDto.getStudentEmail());
            studentEty.setContactNO(studentDto.getContactNO());
            studentEty.setAddress(studentDto.getAddress());
            studentEty.setDateOfBirth(studentDto.getDateOfBirth());
            studentEty.setRegistrationDate(studentDto.getRegistrationDate());
            studentEty.setProgress(studentDto.getProgress());


            List<Course> newCourses = new ArrayList<>();
            for (CourseDto courseDto : courseDtos) {
                Course course = courseDAO.getCourseById(courseDto.getCourseID(),session);
                if (course == null) throw new NotFoundException("Course not found");
                newCourses.add(course);
            }
            studentEty.setCourses(newCourses);


            Payment payment = paymentDAO.findById(paymentDto.getPaymentId(),session);
            if (payment == null) {
                throw new NotFoundException("Payment not found");
            }
            payment.setPaymentType(paymentDto.getPaymentType());
            payment.setPaymentAmount(paymentDto.getPaymentAmount());
            payment.setPaymentDate(paymentDto.getPaymentDate());
            payment.setStatus(paymentDto.getStatus());
            payment.setStudent(studentEty);


            transaction.commit();
            return true;

        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public boolean deleteStudent(int studentID) {
        return studentDAO.delete(studentID);
    }

    @Override
    public boolean checkStudent(String email) {
        try {
            Session session = FactoryConfiguration.getInstance().getSession();
            Transaction transaction = session.beginTransaction();
            return studentDAO.checkStudent(email,session);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public StudentDto getStudentByEmail(String email) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            Student student = studentDAO.getStudentByEmail(email, session);

            tx.commit();

            if (student == null) {
                return null;
            }

            return new StudentDto(
                    student.getStudentID(),
                    student.getStudentName(),
                    student.getStudentEmail(),
                    student.getContactNO(),
                    student.getAddress(),
                    student.getDateOfBirth(),
                    student.getRegistrationDate(),
                    student.getProgress()
            );

        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public StudentDto getStBYId(int studentID) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        StudentDto studentDto = null;

        try {
            tx = session.beginTransaction();

            Student student = studentDAO.findSTById(studentID, session);
            if (student != null) {
                studentDto = new StudentDto(
                        student.getStudentID(),
                        student.getStudentName(),
                        student.getStudentEmail(),
                        student.getContactNO(),
                        student.getAddress(),
                        student.getDateOfBirth(),
                        student.getRegistrationDate(),
                        student.getProgress(),
                        mapCourses(student.getCourses()),
                        null,
                        mapPayments(student.getPayments())
                );
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return studentDto;
    }


}
