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

import static org.mockito.Mockito.*;

public class UserLoginControllerTest {

    @Mock
    private HttpRequest mockRequest;

    @Mock
    private HttpResponse mockResponse;

    private UserLoginController controller;
    private MemoryUserRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UserLoginController();
        repository = MemoryUserRepository.getInstance();

        // 테스트용 사용자 미리 등록
        User testUser = new User("testuser", "1234", "홍길동", "test@example.com");
        repository.addUser(testUser);
    }

    @Test
    @DisplayName("올바른 계정 정보로 로그인하면 메인 페이지로 리다이렉트되어야 한다")
    public void loginSuccessWithValidCredentials() throws IOException {
        // given
        when(mockRequest.getParameters()).thenReturn(java.util.Map.of("userId", "testuser", "password", "1234"));

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).redirectWithCookie(RequestPath.INDEX.getValue(), "logined=true");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인하면 로그인 실패 페이지로 리다이렉트되어야 한다")
    public void loginFailWithWrongPassword() throws IOException {
        // given
        when(mockRequest.getParameters()).thenReturn(java.util.Map.of("userId", "testuser", "password", "wrongpassword"));

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).redirect(RequestPath.USER_LOGIN_FAILED.getValue());
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 로그인하면 로그인 실패 페이지로 리다이렉트되어야 한다")
    public void loginFailWithNonExistentUser() throws IOException {
        // given
        when(mockRequest.getParameters()).thenReturn(java.util.Map.of("userId", "nonexistent", "password", "1234"));

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).redirect(RequestPath.USER_LOGIN_FAILED.getValue());
    }

    @Test
    @DisplayName("빈 파라미터로 로그인하면 로그인 실패 페이지로 리다이렉트되어야 한다")
    public void loginFailWithEmptyParameters() throws IOException {
        // given
        when(mockRequest.getParameters()).thenReturn(java.util.Map.of());

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).redirect(RequestPath.USER_LOGIN_FAILED.getValue());
    }
}