package edu.ijse.dto;

import edu.ijse.entity.Student;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaymentDto {
    private int paymentId;
    private LocalDate paymentDate;
    private String paymentType;
    private BigDecimal paymentAmount;
    private String status;

    //private Course course;
    private Student student;
}
