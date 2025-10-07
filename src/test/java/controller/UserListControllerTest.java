package controller;

import http.HttpRequest;
import http.HttpResponse;
import http.enums.HttpMethod;
import http.enums.RequestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class UserListControllerTest {

    @Mock
    private HttpRequest mockRequest;

    @Mock
    private HttpResponse mockResponse;

    private UserListController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UserListController();
    }

    @Test
    @DisplayName("로그인된 사용자가 사용자 목록에 접근하면 사용자 목록 페이지를 보여줘야 한다")
    public void showUserListForLoggedInUser() throws IOException {
        // given
        when(mockRequest.getCookie("logined")).thenReturn("true");

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).forward(RequestPath.USER_LIST_HTML.getValue());
    }

    @Test
    @DisplayName("로그인되지 않은 사용자가 사용자 목록에 접근하면 로그인 페이지로 리다이렉트되어야 한다")
    public void redirectToLoginForNonLoggedInUser() throws IOException {
        // given
        when(mockRequest.getCookie("logined")).thenReturn(null);

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).redirect(RequestPath.USER_LOGIN_HTML.getValue());
    }

    @Test
    @DisplayName("다른 쿠키가 있지만 로그인 쿠키가 없는 사용자는 로그인 페이지로 리다이렉트되어야 한다")
    public void redirectToLoginForUserWithOtherCookies() throws IOException {
        // given
        when(mockRequest.getCookie("logined")).thenReturn(null);

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).redirect(RequestPath.USER_LOGIN_HTML.getValue());
    }

    @Test
    @DisplayName("logined=true를 포함한 복합 쿠키가 있는 사용자는 사용자 목록을 볼 수 있어야 한다")
    public void showUserListForUserWithComplexCookieIncludingLogined() throws IOException {
        // given
        when(mockRequest.getCookie("logined")).thenReturn("true");

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).forward(RequestPath.USER_LIST_HTML.getValue());
    }
}