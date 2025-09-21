package edu.ijse.BO.custom.impl;

import edu.ijse.BO.custom.InstructorBO;
import edu.ijse.DAO.DAOFactory;
import edu.ijse.DAO.custom.InstructorDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.dto.InstructorDto;
import edu.ijse.entity.Availability_instructor;
import edu.ijse.entity.Instructor;
import edu.ijse.exception.DuplicateEntryException;
import edu.ijse.util.RegEXUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class InstructorBOImpl implements InstructorBO {

    private InstructorDAO instructorDAO=(InstructorDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.INSTRUCTOR);
    @Override
    public boolean saveInstructor(InstructorDto dto) {

        if(!RegEXUtil.isValidName(dto.getInstructorName())){
            throw new IllegalArgumentException("invalid instructor name");
        }
        if (!RegEXUtil.isValidEmail(dto.getInstructorEmail())) {
            throw new IllegalArgumentException("Invalid instructor email");
        }
        Session session= FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx= session.beginTransaction();
        try {

            boolean isInstructorExists =instructorDAO.findInsByEmail(dto.getInstructorEmail(),session);
            if(isInstructorExists){
                throw new DuplicateEntryException("Instructor already exists");
            }
            Instructor instructor = new Instructor();
            instructor.setInstructorName(dto.getInstructorName());
            instructor.setInstructorEmail(dto.getInstructorEmail());
            instructor.setAvailability(Availability_instructor.AVAILABLE);

            boolean result = instructorDAO.save(instructor,session);
            tx.commit();
            return result;

        }catch (Exception e){
            tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean updateInstructor(InstructorDto dto) {

        if(!RegEXUtil.isValidName(dto.getInstructorName())){
            throw new IllegalArgumentException("Invalid instructor name");
        }
        if (!RegEXUtil.isValidEmail(dto.getInstructorEmail())) {
            throw new IllegalArgumentException("Invalid instructor email");
        }

        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {

            Instructor existingInstructor = instructorDAO.findById(dto.getInstructorID(), session);
            if (existingInstructor == null) {
                throw new IllegalArgumentException("Instructor not found with ID: " + dto.getInstructorID());
            }


            if (!existingInstructor.getInstructorEmail().equals(dto.getInstructorEmail())) {
                boolean emailExists = instructorDAO.findInsByEmail(dto.getInstructorEmail(), session);
                if (emailExists) {
                    throw new IllegalArgumentException("Email already in use by another instructor");
                }
            }


            existingInstructor.setInstructorName(dto.getInstructorName());
            existingInstructor.setInstructorEmail(dto.getInstructorEmail());


            if (dto.getInstructorAvailability() != null) {
                existingInstructor.setAvailability(Availability_instructor.valueOf(dto.getInstructorAvailability()));
            }


            boolean result = instructorDAO.update(existingInstructor, session);

            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }


    @Override
    public boolean deleteInstructor(int id) {
        return instructorDAO.delete(id);
    }

    @Override
    public List<InstructorDto> getAllInstructors() {
        List<InstructorDto> instructorDtoList=new ArrayList<>();
        List<Instructor> instructors;
        try {
            instructors=instructorDAO.getAll();
            for(Instructor instructor:instructors){
                instructorDtoList.add(new InstructorDto(instructor.getInstructorID(),instructor.getInstructorName(),instructor.getInstructorEmail(),
                        instructor.getAvailability().toString()));
            }
            return instructorDtoList;
        } catch (Exception e) {
            throw e;
        }

    }
}
