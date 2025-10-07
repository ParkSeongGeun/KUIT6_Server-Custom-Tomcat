package webserver;

import controller.*;
import http.enums.HttpMethod;
import http.enums.RequestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RequestMapper 테스트")
class RequestMapperTest {

    private RequestMapper requestMapper;

    @BeforeEach
    void setUp() {
        requestMapper = new RequestMapper();
    }

    @Test
    @DisplayName("루트 경로 요청 시 ForwardController를 반환한다")
    void shouldReturnForwardControllerWhenRootPath() {
        // given
        String path = RequestPath.ROOT.getValue();
        HttpMethod method = HttpMethod.GET;

        // when
        Controller controller = requestMapper.getController(path, method);

        // then
        assertInstanceOf(ForwardController.class, controller);
    }

    @Test
    @DisplayName("POST /user/signup 요청 시 UserSignupController를 반환한다")
    void shouldReturnUserSignupControllerWhenPostUserSignup() {
        // given
        String path = RequestPath.USER_SIGNUP.getValue();
        HttpMethod method = HttpMethod.POST;

        // when
        Controller controller = requestMapper.getController(path, method);

        // then
        assertInstanceOf(UserSignupController.class, controller);
    }

    @Test
    @DisplayName("POST /user/login 요청 시 UserLoginController를 반환한다")
    void shouldReturnUserLoginControllerWhenPostUserLogin() {
        // given
        String path = RequestPath.USER_LOGIN.getValue();
        HttpMethod method = HttpMethod.POST;

        // when
        Controller controller = requestMapper.getController(path, method);

        // then
        assertInstanceOf(UserLoginController.class, controller);
    }

    @Test
    @DisplayName("/user/userList 요청 시 UserListController를 반환한다")
    void shouldReturnUserListControllerWhenUserList() {
        // given
        String path = RequestPath.USER_LIST.getValue();
        HttpMethod method = HttpMethod.GET;

        // when
        Controller controller = requestMapper.getController(path, method);

        // then
        assertInstanceOf(UserListController.class, controller);
    }

    @Test
    @DisplayName("알 수 없는 경로 요청 시 ForwardController를 기본으로 반환한다")
    void shouldReturnForwardControllerWhenUnknownPath() {
        // given
        String path = "/unknown/path";
        HttpMethod method = HttpMethod.GET;

        // when
        Controller controller = requestMapper.getController(path, method);

        // then
        assertInstanceOf(ForwardController.class, controller);
    }
}