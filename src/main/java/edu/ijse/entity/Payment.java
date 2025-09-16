package edu.ijse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;
    private LocalDate paymentDate;
    private String paymentType;
    private BigDecimal paymentAmount;
    private String status;
    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    public Payment(int paymentId, LocalDate paymentDate, BigDecimal paymentAmount, Student student, Course course) {
    }

    public Payment(int paymentId, LocalDate paymentDate, String paymentType, BigDecimal paymentAmount, String status) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.paymentAmount = paymentAmount;
        this.status = status;
    }
}
