package webserver;

import controller.*;
import http.enums.HttpMethod;

import java.util.Map;

public class RequestMapper {
    private static final Map<String, Controller> CONTROLLERS = WebConfig.configureControllers();

    public Controller getController(String path, HttpMethod method) {
        // 먼저 path + method 조합으로 찾기
        String key = WebConfig.createKey(path, method);
        Controller controller = CONTROLLERS.get(key);

        // 없으면 path만으로 찾기
        if (controller == null) {
            controller = CONTROLLERS.get(path);
        }

        // 매핑된 컨트롤러가 없으면 ForwardController 반환
        return controller != null ? controller : new ForwardController();
    }
}