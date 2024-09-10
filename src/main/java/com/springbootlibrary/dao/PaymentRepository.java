package com.springbootlibrary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springbootlibrary.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    Payment findByUserEmail(String useEmail);
}
