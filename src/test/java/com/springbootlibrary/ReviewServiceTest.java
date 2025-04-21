
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   
    @Test
    void testPostReview_shouldThrowWhenAlreadyReviewed() {
        // Given
        String userEmail = "user@example.com";
        Long bookId = 1L;

        ReviewRequest request = new ReviewRequest();
        request.setBookId(bookId);
        request.setRating(4);
        request.setReviewDescription(Optional.of("Nice"));

        when(reviewRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(new Review());

        // When + Then
        Exception ex = assertThrows(Exception.class, () ->
                reviewService.postReview(userEmail, request)
        );

        assertEquals("Review already created", ex.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    

    @Test
    void testUserReviewListed_shouldReturnFalse() {
        // Given
        String userEmail = "user@example.com";
        Long bookId = 2L;

        when(reviewRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(null);

        // When
        Boolean result = reviewService.userReviewListed(userEmail, bookId);

        // Then
        assertFalse(result);
    }
}

