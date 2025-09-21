package edu.ijse.BO.custom;

import edu.ijse.BO.SuperBO;
import edu.ijse.dto.CourseDto;
import edu.ijse.dto.PaymentDto;
import edu.ijse.dto.StudentDto;

import java.util.ArrayList;
import java.util.List;

public interface StudentBO  extends SuperBO {
    public boolean saveStudent(StudentDto student, List<CourseDto> courseDtos, PaymentDto paymentDto);
    public ArrayList<StudentDto> loadAllStudent();
    public boolean updateStudent(StudentDto student,List<CourseDto> courseDtos,PaymentDto paymentDto);
    public boolean deleteStudent(int studentID);
    boolean checkStudent(String email);
    StudentDto getStudentByEmail(String email);
    StudentDto getStBYId(int studentID);
}
