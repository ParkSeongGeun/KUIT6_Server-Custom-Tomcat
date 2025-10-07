package controller;

import http.HttpRequest;
import http.HttpResponse;
import http.enums.RequestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ForwardControllerTest {

    @Mock
    private HttpRequest mockRequest;

    @Mock
    private HttpResponse mockResponse;

    private final ForwardController controller = new ForwardController();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("루트 경로(/)로 요청하면 인덱스 페이지로 forward되어야 한다")
    public void forwardToIndexForRootPath() throws IOException {
        // given
        when(mockRequest.getPath()).thenReturn("/");

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).forward(RequestPath.INDEX.getValue());
    }

    @Test
    @DisplayName("일반 정적 파일 경로로 요청하면 해당 파일로 forward되어야 한다")
    public void forwardToStaticFile() throws IOException {
        // given
        when(mockRequest.getPath()).thenReturn("/css/style.css");

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).forward("/css/style.css");
    }

    @Test
    @DisplayName("디렉토리 트래버설 공격(..)이 포함된 경로는 404 응답을 해야 한다")
    public void returnNotFoundForDirectoryTraversalAttack() throws IOException {
        // given
        when(mockRequest.getPath()).thenReturn("/../../etc/passwd");

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).notFound();
        verify(mockResponse, never()).forward(anyString());
    }

    @Test
    @DisplayName("파일이 존재하지 않으면 404 응답을 해야 한다")
    public void returnNotFoundForNonExistentFile() throws IOException {
        // given
        when(mockRequest.getPath()).thenReturn("/nonexistent.html");
        doThrow(new IOException("File not found")).when(mockResponse).forward("/nonexistent.html");

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).forward("/nonexistent.html");
        verify(mockResponse).notFound();
    }

    @Test
    @DisplayName("HTML 파일 요청은 정상적으로 forward되어야 한다")
    public void forwardHtmlFile() throws IOException {
        // given
        when(mockRequest.getPath()).thenReturn("/user/form.html");

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).forward("/user/form.html");
    }

    @Test
    @DisplayName("JS 파일 요청은 정상적으로 forward되어야 한다")
    public void forwardJsFile() throws IOException {
        // given
        when(mockRequest.getPath()).thenReturn("/js/app.js");

        // when
        controller.execute(mockRequest, mockResponse);

        // then
        verify(mockResponse).forward("/js/app.js");
    }
}