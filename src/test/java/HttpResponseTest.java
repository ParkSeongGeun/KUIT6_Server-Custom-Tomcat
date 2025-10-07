import http.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class HttpResponseTest {
    
    private static final String TEST_DIRECTORY = "src/test/resources/response/";

    private OutputStream outputStreamToFile(String path) throws IOException {
        return Files.newOutputStream(Paths.get(path));
    }

    @Test
    @DisplayName("forward 메서드로 HTML 파일을 전송할 때 올바른 HTTP 응답이 생성되어야 한다")
    public void forwardHtmlFileWithCorrectHttpResponse() throws IOException {
        // given
        String outputPath = TEST_DIRECTORY + "forward_output.txt";
        HttpResponse httpResponse = new HttpResponse(outputStreamToFile(outputPath));

        // when
        httpResponse.forward("/index.html");

        // then
        String responseContent = Files.readString(Paths.get(outputPath));
        assertTrue(responseContent.contains("HTTP/1.1 200 OK"));
        assertTrue(responseContent.contains("Content-Type: text/html"));
        assertTrue(responseContent.contains("Content-Length:"));

        // cleanup
        Files.deleteIfExists(Paths.get(outputPath));
    }

    @Test
    @DisplayName("redirect 메서드로 리다이렉트할 때 302 응답과 Location 헤더가 생성되어야 한다")
    public void redirectWithCorrectLocationHeader() throws IOException {
        // given
        String outputPath = TEST_DIRECTORY + "redirect_output.txt";
        HttpResponse httpResponse = new HttpResponse(outputStreamToFile(outputPath));
        
        // when
        httpResponse.redirect("/index.html");
        
        // then
        String responseContent = Files.readString(Paths.get(outputPath));
        assertTrue(responseContent.contains("HTTP/1.1 302 Found"));
        assertTrue(responseContent.contains("Location: /index.html"));
        
        // cleanup
        Files.deleteIfExists(Paths.get(outputPath));
    }

    @Test
    @DisplayName("쿠키와 함께 리다이렉트할 때 Set-Cookie 헤더가 포함되어야 한다")
    public void redirectWithCookieIncludesSetCookieHeader() throws IOException {
        // given
        String outputPath = TEST_DIRECTORY + "redirect_cookie_output.txt";
        HttpResponse httpResponse = new HttpResponse(outputStreamToFile(outputPath));
        
        // when
        httpResponse.redirectWithCookie("/index.html", "logined=true");
        
        // then
        String responseContent = Files.readString(Paths.get(outputPath));
        assertTrue(responseContent.contains("HTTP/1.1 302 Found"));
        assertTrue(responseContent.contains("Set-Cookie: logined=true"));
        assertTrue(responseContent.contains("Location: /index.html"));
        
        // cleanup
        Files.deleteIfExists(Paths.get(outputPath));
    }

    @Test
    @DisplayName("404 에러 응답을 생성할 때 올바른 에러 메시지가 포함되어야 한다")
    public void notFoundResponseWithErrorMessage() throws IOException {
        // given
        String outputPath = TEST_DIRECTORY + "notfound_output.txt";
        HttpResponse httpResponse = new HttpResponse(outputStreamToFile(outputPath));
        
        // when
        httpResponse.notFound();
        
        // then
        String responseContent = Files.readString(Paths.get(outputPath));
        assertTrue(responseContent.contains("HTTP/1.1 404 Not Found"));
        assertTrue(responseContent.contains("Content-Type: text/html"));
        assertTrue(responseContent.contains("404 Not Found"));
        
        // cleanup
        Files.deleteIfExists(Paths.get(outputPath));
    }
}