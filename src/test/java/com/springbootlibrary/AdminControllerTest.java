package com.springbootlibrary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootlibrary.requestmodels.AddBookRequest;
import com.springbootlibrary.service.AdminService;
import com.springbootlibrary.utils.ExtractJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import  com.springbootlibrary.controller.AdminController;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    private final String ADMIN_TOKEN = "Bearer adminToken";
    private final String NON_ADMIN_TOKEN = "Bearer userToken";

    private AddBookRequest sampleBookRequest;

    @BeforeEach
    void setUp() {
        sampleBookRequest = new AddBookRequest();
        sampleBookRequest.setTitle("Test Book");
        sampleBookRequest.setAuthor("Test Author");
        sampleBookRequest.setDescription("Test Description");
        sampleBookRequest.setCopies(5);
        // Add other fields if needed
    }

    @Test
    void postBook_withAdminToken_shouldCallService() throws Exception {
        try (MockedStatic<ExtractJWT> mockedJwt = Mockito.mockStatic(ExtractJWT.class)) {
            mockedJwt.when(() -> ExtractJWT.payloadJWTExtraction(ADMIN_TOKEN, "\"userType\""))
                    .thenReturn("admin");

            mockMvc.perform(post("/api/admin/secure/add/book")
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(sampleBookRequest)))
                    .andExpect(status().isOk());

            verify(adminService, times(1)).postBook(any(AddBookRequest.class));
        }
    }

    @Test
    void postBook_withNonAdminToken_shouldReturnForbidden() throws Exception {
        try (MockedStatic<ExtractJWT> mockedJwt = Mockito.mockStatic(ExtractJWT.class)) {
            mockedJwt.when(() -> ExtractJWT.payloadJWTExtraction(NON_ADMIN_TOKEN, "\"userType\""))
                    .thenReturn("user");

            mockMvc.perform(post("/api/admin/secure/add/book")
                            .header("Authorization", NON_ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(sampleBookRequest)))
                    .andExpect(status().isInternalServerError());  // change to 403 if you use custom exception handling
        }
    }

    @Test
    void increaseBookQuantity_withAdminToken_shouldCallService() throws Exception {
        try (MockedStatic<ExtractJWT> mockedJwt = Mockito.mockStatic(ExtractJWT.class)) {
            mockedJwt.when(() -> ExtractJWT.payloadJWTExtraction(ADMIN_TOKEN, "\"userType\""))
                    .thenReturn("admin");

            mockMvc.perform(put("/api/admin/secure/increase/book/quantity")
                            .header("Authorization", ADMIN_TOKEN)
                            .param("bookId", "1"))
                    .andExpect(status().isOk());

            verify(adminService, times(1)).increaseBookQuantity(1L);
        }
    }

    @Test
    void decreaseBookQuantity_withAdminToken_shouldCallService() throws Exception {
        try (MockedStatic<ExtractJWT> mockedJwt = Mockito.mockStatic(ExtractJWT.class)) {
            mockedJwt.when(() -> ExtractJWT.payloadJWTExtraction(ADMIN_TOKEN, "\"userType\""))
                    .thenReturn("admin");

            mockMvc.perform(put("/api/admin/secure/decrease/book/quantity")
                            .header("Authorization", ADMIN_TOKEN)
                            .param("bookId", "2"))
                    .andExpect(status().isOk());

            verify(adminService, times(1)).decreaseBookQuantity(2L);
        }
    }

    @Test
    void deleteBook_withAdminToken_shouldCallService() throws Exception {
        try (MockedStatic<ExtractJWT> mockedJwt = Mockito.mockStatic(ExtractJWT.class)) {
            mockedJwt.when(() -> ExtractJWT.payloadJWTExtraction(ADMIN_TOKEN, "\"userType\""))
                    .thenReturn("admin");

            mockMvc.perform(delete("/api/admin/secure/delete/book")
                            .header("Authorization", ADMIN_TOKEN)
                            .param("bookId", "3"))
                    .andExpect(status().isOk());

            verify(adminService, times(1)).deleteBook(3L);
        }
    }
}
