package edu.ijse.tm;

import edu.ijse.entity.Course;
import edu.ijse.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentTM {
    private int paymentId;
    private LocalDate paymentDate;
    private String paymentType;
    private BigDecimal paymentAmount;
    private String status;

    private Course course;
    private Student student;
}
