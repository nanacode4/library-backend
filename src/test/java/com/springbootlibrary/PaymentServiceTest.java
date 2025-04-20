package com.springbootlibrary;
import com.springbootlibrary.service.PaymentService;
import com.springbootlibrary.dao.PaymentRepository;
import com.springbootlibrary.entity.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testStripePayment_success() throws Exception {
        // Given
        Payment payment = new Payment();
        payment.setUserEmail("user@example.com");
        payment.setAmount(20.00);

        when(paymentRepository.findByUserEmail("user@example.com")).thenReturn(payment);

        // When
        ResponseEntity<String> response = paymentService.stripePayment("user@example.com");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.00, payment.getAmount());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testStripePayment_paymentNotFound() {
        // Given
        when(paymentRepository.findByUserEmail("user@example.com")).thenReturn(null);

        // When + Then
        Exception exception = assertThrows(Exception.class, () -> {
            paymentService.stripePayment("user@example.com");
        });

        assertEquals("Payment information is missing", exception.getMessage());
        verify(paymentRepository, never()).save(any());
    }
}

