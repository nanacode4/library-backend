package com.springbootlibrary;

import  com.springbootlibrary.service.BookService;
import com.springbootlibrary.dao.*;
import com.springbootlibrary.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CheckoutRepository checkoutRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ===================== TEST checkoutBook =====================

    @Test
    void testCheckoutBook_success() throws Exception {
        // Given
        Long bookId = 1L;
        String userEmail = "user@example.com";
        Book book = new Book();
        book.setId(bookId);
        book.setCopiesAvailable(3);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(checkoutRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(null);
        when(checkoutRepository.findBooksByUserEmail(userEmail)).thenReturn(new ArrayList<>());
        when(paymentRepository.findByUserEmail(userEmail)).thenReturn(null);

        // When
        Book result = bookService.checkoutBook(userEmail, bookId);

        // Then
        assertEquals(2, result.getCopiesAvailable());
        verify(bookRepository).save(book);
        verify(checkoutRepository).save(any(Checkout.class));
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testCheckoutBook_failsDueToAlreadyCheckedOut() {
        // Given
        String userEmail = "user@example.com";
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setCopiesAvailable(2);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(checkoutRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(new Checkout());

        // When + Then
        Exception ex = assertThrows(Exception.class, () ->
                bookService.checkoutBook(userEmail, bookId)
        );
        assertTrue(ex.getMessage().contains("Book doesn't exist or already checked out"));
    }

    // ===================== TEST returnBook =====================

    @Test
    void testReturnBook_success() throws Exception {
        // Given
        String userEmail = "user@example.com";
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setCopiesAvailable(1);

        Checkout checkout = new Checkout();
        checkout.setBookId(bookId);
        checkout.setUserEmail(userEmail);
        checkout.setCheckoutDate(LocalDate.now().minusDays(7).toString());
        checkout.setReturnDate(LocalDate.now().toString());
        checkout.setId(100L);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(checkoutRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(checkout);
        when(paymentRepository.findByUserEmail(userEmail)).thenReturn(new Payment());

        // When
        bookService.returnBook(userEmail, bookId);

        // Then
        verify(bookRepository).save(book);
        verify(checkoutRepository).deleteById(checkout.getId());
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void testReturnBook_failsWhenNotCheckedOut() {
        // Given
        String userEmail = "user@example.com";
        Long bookId = 2L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        when(checkoutRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(null);

        // When + Then
        Exception ex = assertThrows(Exception.class, () ->
                bookService.returnBook(userEmail, bookId)
        );

        assertTrue(ex.getMessage().contains("Book does not exist or not checked out"));
    }

    // ===================== TEST renewLoan =====================

    @Test
    void testRenewLoan_success() throws Exception {
        // Given
        String userEmail = "user@example.com";
        Long bookId = 3L;
        Checkout checkout = new Checkout();
        checkout.setBookId(bookId);
        checkout.setUserEmail(userEmail);
        checkout.setReturnDate(LocalDate.now().plusDays(1).toString());

        when(checkoutRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(checkout);

        // When
        bookService.renewLoan(userEmail, bookId);

        // Then
        verify(checkoutRepository).save(checkout);
        assertEquals(LocalDate.now().plusDays(7).toString(), checkout.getReturnDate());
    }

    @Test
    void testRenewLoan_failsWhenNoCheckout() {
        // Given
        String userEmail = "user@example.com";
        Long bookId = 4L;

        when(checkoutRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(null);

        // When + Then
        Exception ex = assertThrows(Exception.class, () ->
                bookService.renewLoan(userEmail, bookId)
        );
        assertEquals("Book does not exist or not checked out by user", ex.getMessage());
    }

    // ===================== Other Utility Methods =====================

    @Test
    void testCheckoutBookByUser_returnsTrue() {
        when(checkoutRepository.findByUserEmailAndBookId("user@example.com", 1L))
                .thenReturn(new Checkout());

        assertTrue(bookService.checkoutBookByUser("user@example.com", 1L));
    }

    @Test
    void testCheckoutBookByUser_returnsFalse() {
        when(checkoutRepository.findByUserEmailAndBookId("user@example.com", 1L))
                .thenReturn(null);

        assertFalse(bookService.checkoutBookByUser("user@example.com", 1L));
    }

    @Test
    void testCurrentLoansCount() {
        List<Checkout> checkouts = Arrays.asList(new Checkout(), new Checkout());
        when(checkoutRepository.findBooksByUserEmail("user@example.com")).thenReturn(checkouts);

        int count = bookService.currentLoansCount("user@example.com");

        assertEquals(2, count);
    }

}

