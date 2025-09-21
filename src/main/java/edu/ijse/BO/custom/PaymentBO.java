package edu.ijse.BO.custom;

import edu.ijse.BO.SuperBO;
import edu.ijse.dto.PaymentDto;

import java.util.List;

public interface PaymentBO extends SuperBO {
    boolean savePayment(PaymentDto dto,int studentId);
    boolean updatePayment(PaymentDto dto);
    boolean deletePayment(int id);
    List<PaymentDto> getAllPayments();
}
