package edu.ijse.BO.custom.impl;

import edu.ijse.BO.custom.PaymentBO;
import edu.ijse.DAO.DAOFactory;
import edu.ijse.DAO.custom.PaymentDAO;
import edu.ijse.DAO.custom.StudentDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.dto.PaymentDto;
import edu.ijse.entity.Payment;
import edu.ijse.entity.Student;
import edu.ijse.exception.NotFoundException;
import edu.ijse.exception.PaymentInsertionException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentBOImpl implements PaymentBO {

    private StudentDAO studentDAO=(StudentDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.STUDENT);
    private PaymentDAO paymentDAO=(PaymentDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.PAYMENT);
    @Override
    public boolean savePayment(PaymentDto dto,int studentId) {
        if (dto.getPaymentType() == null || dto.getPaymentType().isBlank()) {
            throw new IllegalArgumentException("Payment type cannot be empty");
        }
        if (dto.getPaymentAmount() == null || dto.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        Session session= FactoryConfiguration.getInstance().getSession();
        Transaction tx= session.beginTransaction();
        try {
            Student student=studentDAO.findSTById(studentId,session);
            if(student==null){
                throw new NotFoundException("this student not found");
            }

            Payment payment=new Payment();
            payment.setStudent(student);
            payment.setPaymentDate(LocalDate.now());
            payment.setPaymentType(dto.getPaymentType());
            payment.setPaymentAmount(dto.getPaymentAmount());
            payment.setStatus(dto.getStatus());

            boolean paymentDone=paymentDAO.save(payment,session);
            if(!paymentDone){
                throw  new PaymentInsertionException("payment failed");
            }
            tx.commit();
            return true;
        }catch (Exception e){
            tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean updatePayment(PaymentDto dto) {
        if (dto.getPaymentId() <= 0) {
            throw new IllegalArgumentException("Invalid payment ID");
        }
        if (dto.getPaymentType() == null || dto.getPaymentType().isBlank()) {
            throw new IllegalArgumentException("Payment type cannot be empty");
        }
        if (dto.getPaymentAmount() == null || dto.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            Payment existingPayment = paymentDAO.findById(dto.getPaymentId(), session);
            if (existingPayment == null) {
                throw new NotFoundException("Payment not found with ID: " + dto.getPaymentId());
            }

            existingPayment.setPaymentType(dto.getPaymentType());
            existingPayment.setPaymentAmount(dto.getPaymentAmount());
            existingPayment.setStatus(dto.getStatus());
            existingPayment.setPaymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDate.now());

            boolean updated = paymentDAO.update(existingPayment, session);
            tx.commit();
            return updated;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean deletePayment(int id) {
        return paymentDAO.delete(id);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            List<Payment> paymentList = paymentDAO.getAll();
            List<PaymentDto> dtoList = new ArrayList<>();

            for (Payment payment : paymentList) {
                dtoList.add(PaymentDto.builder()
                        .paymentId(payment.getPaymentId())
                        .paymentType(payment.getPaymentType())
                        .paymentAmount(payment.getPaymentAmount())
                        .status(payment.getStatus())
                        .paymentDate(payment.getPaymentDate())
                        .student(payment.getStudent())
                        .build());
            }

            tx.commit();
            return dtoList;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
    }

