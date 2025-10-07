package controller;

import db.MemoryUserRepository;
import http.HttpRequest;
import http.HttpResponse;
import http.enums.RequestPath;
import model.User;
import model.UserField;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserLoginController implements Controller {
    private static final Logger log = Logger.getLogger(UserLoginController.class.getName());

    @Override
    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> params = request.getParameters();

        String userId = params.get(UserField.USER_ID.getValue());
        String password = params.get(UserField.PASSWORD.getValue());

        if (userId != null && !userId.isEmpty()) {
            log.log(Level.INFO, "Login attempt: " + userId);
        }

        // 파라미터 유효성 검사
        if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
            log.log(Level.WARNING, "Login failed: missing parameters");
            response.redirect(RequestPath.USER_LOGIN_FAILED.getValue());
            return;
        }

        // MemoryUserRepository에서 사용자 조회
        MemoryUserRepository repository = MemoryUserRepository.getInstance();
        User user = repository.findUserById(userId);

        // 인증 검증
        if (user != null && user.getPassword().equals(password)) {
            // 로그인 성공: Cookie 설정 + 메인페이지로 리다이렉트
            log.log(Level.INFO, "Login successful: " + userId);
            response.redirectWithCookie(RequestPath.INDEX.getValue(), "logined=true");
        } else {
            // 로그인 실패: 에러페이지로 리다이렉트
            log.log(Level.WARNING, "Login failed: " + userId);
            response.redirect(RequestPath.USER_LOGIN_FAILED.getValue());
        }
    }
}