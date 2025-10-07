package controller;

import db.MemoryUserRepository;
import http.HttpRequest;
import http.HttpResponse;
import http.enums.HttpMethod;
import http.enums.RequestPath;
import model.User;
import model.UserField;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSignupController implements Controller {
    private static final Logger log = Logger.getLogger(UserSignupController.class.getName());

    @Override
    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        // 파라미터 추출
        Map<String, String> params = request.getParameters();
        log.log(Level.INFO, "POST Signup params: " + params);

        // 필수 필드 검증
        String userId = params.get (UserField.USER_ID.getValue());
        String password = params.get (UserField. PASSWORD.getValue());
        String name = params.get (UserField.NAME.getValue());
        String email = params.get (UserField.EMAIL.getValue());

        if (userId == null || userId.trim().isEmpty() ||
                password == null || password. length() < 4 ||
                name == null || name.trim().isEmpty() ||
                email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            // TODO: Bad Request
            return;
        }

        // User 객체 생성
        User newUser = new User(userId, password, name, email);

        // 메모리 저장소에 저장
        MemoryUserRepository repository = MemoryUserRepository.getInstance();
        repository.addUser(newUser);
        log.log(Level.INFO, "New User Registered: " + newUser.getUserId());

        // 302 리다이렉트로 메인 페이지로 이동
        response.redirect(RequestPath.INDEX.getValue());
    }
}