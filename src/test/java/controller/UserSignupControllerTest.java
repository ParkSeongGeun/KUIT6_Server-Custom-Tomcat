package controller;

import db.MemoryUserRepository;
import http.HttpRequest;
import http.HttpResponse;
import http.enums.HttpMethod;
import http.enums.RequestPath;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserSignupControllerTest {

    @Mock
    private HttpRequest mockRequest;

    @Mock
    private HttpResponse mockResponse;

    private UserSignupController controller;
    private MemoryUserRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UserSignupController();
        repository = MemoryUserRepository.getInstance();
    }

    @Test
    @DisplayName("POST 요청으로 유효한 사용자 정보를 전송하면 회원가입이 성공해야 한다")
    public void signupSuccessWithPostRequest() throws IOException {
        // given
        when(mockRequest.getParameters()).thenReturn(java.util.Map.of(
            "userId", "testuser",
            "password", "1234",
            "name", "홍길동",
            "email", "test@example.com"
        ));

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).redirect(RequestPath.INDEX.getValue());

        User savedUser = repository.findUserById("testuser");
        assertNotNull(savedUser, "사용자가 저장되어야 한다");
        assertEquals("testuser", savedUser.getUserId());
        assertEquals("1234", savedUser.getPassword());
        assertEquals("홍길동", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("POST 요청으로 다른 유효한 사용자 정보를 전송하면 회원가입이 성공해야 한다")
    public void signupSuccessWithAnotherPostRequest() throws IOException {
        // given
        when(mockRequest.getParameters()).thenReturn(java.util.Map.of(
            "userId", "getuser",
            "password", "5678",
            "name", "김철수",
            "email", "get@example.com"
        ));

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).redirect(RequestPath.INDEX.getValue());

        User savedUser = repository.findUserById("getuser");
        assertNotNull(savedUser, "사용자가 저장되어야 한다");
        assertEquals("getuser", savedUser.getUserId());
        assertEquals("5678", savedUser.getPassword());
        assertEquals("김철수", savedUser.getName());
        assertEquals("get@example.com", savedUser.getEmail());
    }
}