package com.springbootlibrary;
import com.springbootlibrary.service.MessageService;
import com.springbootlibrary.dao.MessageRepository;
import com.springbootlibrary.entity.Message;
import com.springbootlibrary.requestmodels.AdminQuestionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostMessage_shouldSaveMessage() {
        // Given
        Message messageRequest = new Message();
        messageRequest.setTitle("Help");
        messageRequest.setQuestion("How do I borrow a book?");
        String userEmail = "user@example.com";

        // When
        messageService.postMessage(messageRequest, userEmail);

        // Then
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository, times(1)).save(captor.capture());

        Message savedMessage = captor.getValue();
        assertEquals("Help", savedMessage.getTitle());
        assertEquals("How do I borrow a book?", savedMessage.getQuestion());
        assertEquals("user@example.com", savedMessage.getUserEmail());
    }

    @Test
    void testPutMessage_shouldUpdateMessageSuccessfully() throws Exception {
        // Given
        AdminQuestionRequest request = new AdminQuestionRequest();
        request.setId(1L);
        request.setResponse("Here is how to borrow a book.");

        Message existingMessage = new Message();
        existingMessage.setId(1L);
        existingMessage.setTitle("Help");
        existingMessage.setQuestion("How do I borrow a book?");
        existingMessage.setUserEmail("user@example.com");

        when(messageRepository.findById(1L)).thenReturn(Optional.of(existingMessage));

        // When
        messageService.putMessage(request, "admin@example.com");

        // Then
        assertEquals("admin@example.com", existingMessage.getAdminEmail());
        assertEquals("Here is how to borrow a book.", existingMessage.getResponse());
        verify(messageRepository).save(existingMessage);
    }

    @Test
    void testPutMessage_shouldThrowExceptionWhenMessageNotFound() {
        // Given
        AdminQuestionRequest request = new AdminQuestionRequest();
        request.setId(1L);
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        // When + Then
        Exception exception = assertThrows(Exception.class, () ->
                messageService.putMessage(request, "admin@example.com")
        );

        assertEquals("Message not found", exception.getMessage());
        verify(messageRepository, never()).save(any());
    }
}
