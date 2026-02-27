package com.cinebook.payment.entity;

import java.time.LocalDateTime;

import com.cinebook.payment.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @SequenceGenerator(name = "payment_sq",allocationSize = 1,initialValue = 1000,sequenceName = "payment_sq")
    @GeneratedValue(generator = "payment_sq",strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId;

    @Column(nullable = false)
    private Long bookingId;   // just store ID

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private String paymentMethod;      // CARD, UPI, NETBANKING, WALLET

    private String provider;           // RAZORPAY, STRIPE, PAYPAL etc

    private String providerPaymentId;  // gateway transaction reference id

    private String failureReason;      // store reason if failed

    private String currency;           // INR, USD

    private LocalDateTime paymentTime; // when payment completed
}
	