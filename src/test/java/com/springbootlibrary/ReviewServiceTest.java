package com.springbootlibrary;
import com.springbootlibrary.service.ReviewService;
import com.springbootlibrary.dao.ReviewRepository;
import com.springbootlibrary.entity.Review;
import com.springbootlibrary.requestmodels.ReviewRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
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
    void testPostReview_shouldSaveReview() throws Exception {
        // Given
        String userEmail = "user@example.com";
        Long bookId = 1L;

        ReviewRequest request = new ReviewRequest();
        request.setBookId(bookId);
        request.setRating(5);
        request.setReviewDescription(Optional.of("Amazing book!"));

        when(reviewRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(null);

        // When
        reviewService.postReview(userEmail, request);

        // Then
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository, times(1)).save(reviewCaptor.capture());

        Review saved = reviewCaptor.getValue();
        assertEquals(userEmail, saved.getUserEmail());
        assertEquals(bookId, saved.getBookId());
        assertEquals(5, saved.getRating());
        assertEquals("Amazing book!", saved.getReviewDescription());
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
    void testUserReviewListed_shouldReturnTrue() {
        // Given
        String userEmail = "user@example.com";
        Long bookId = 1L;

        when(reviewRepository.findByUserEmailAndBookId(userEmail, bookId)).thenReturn(new Review());

        // When
        Boolean result = reviewService.userReviewListed(userEmail, bookId);

        // Then
        assertTrue(result);
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

