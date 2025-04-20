package com.springbootlibrary;
import com.springbootlibrary.dao.BookRepository;
import com.springbootlibrary.dao.CheckoutRepository;
import com.springbootlibrary.dao.ReviewRepository;
import com.springbootlibrary.entity.Book;
import com.springbootlibrary.requestmodels.AddBookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.springbootlibrary.service.AdminService;


public class AdminServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CheckoutRepository checkoutRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostBook_savesBook() {
        AddBookRequest request = new AddBookRequest();
        request.setTitle("Test Book");
        request.setAuthor("Author");
        request.setDescription("Test description");
        request.setCopies(10);
        request.setCategory("Fiction");
        request.setImg("test.jpg");

        adminService.postBook(request);

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testIncreaseBookQuantity_success() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setCopies(5);
        book.setCopiesAvailable(3);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        adminService.increaseBookQuantity(1L);

        assertEquals(6, book.getCopies());
        assertEquals(4, book.getCopiesAvailable());
        verify(bookRepository).save(book);
    }

    @Test
    void testIncreaseBookQuantity_bookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            adminService.increaseBookQuantity(1L);
        });

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void testDecreaseBookQuantity_success() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setCopies(5);
        book.setCopiesAvailable(3);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        adminService.decreaseBookQuantity(1L);

        assertEquals(4, book.getCopies());
        assertEquals(2, book.getCopiesAvailable());
        verify(bookRepository).save(book);
    }

    @Test
    void testDeleteBook_success() throws Exception {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        adminService.deleteBook(1L);

        verify(bookRepository).delete(book);
        verify(checkoutRepository).deleteAllByBookId(1L);
        verify(reviewRepository).deleteAllByBookId(1L);
    }

    @Test
    void testDeleteBook_bookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            adminService.deleteBook(1L);
        });

        assertEquals("Book not found", exception.getMessage());
    }
}
