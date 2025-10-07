package http;

import http.enums.ContentType;
import http.enums.HttpHeader;
import http.enums.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponse {
    private static final String WEBAPP_PATH = "webapp";
    private static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";
    private final DataOutputStream dos;
    private final String httpVersion;

    public HttpResponse(OutputStream outputStream) {
        this(outputStream, DEFAULT_HTTP_VERSION);
    }

    public HttpResponse(OutputStream outputStream, String httpVersion) {
        this.dos = new DataOutputStream(outputStream);
        this.httpVersion = httpVersion;
    }

    public void forward(String path) throws IOException {
        // 경로 정규화 및 검증
        if (path.contains("..")) {
            notFound();
            return;
        }

        String filePath = WEBAPP_PATH + path;

        // 파일 존재 확인
        if (!Files.exists(Paths.get(filePath))) {
            notFound();
            return;
        }

        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
        String contentType = ContentType.fromFileExtension(filePath).getValue();
        
        writeStatusLine(HttpStatus.OK);
        writeHeader(HttpHeader.CONTENT_TYPE, contentType);
        writeHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(fileContent.length));
        writeEndOfHeaders();
        writeBody(fileContent);
    }

    public void redirect(String path) throws IOException {
        writeStatusLine(HttpStatus.FOUND);
        writeHeader(HttpHeader.LOCATION, path);
        writeEndOfHeaders();
    }

    public void redirectWithCookie(String path, String cookieValue) throws IOException {
        writeStatusLine(HttpStatus.FOUND);
        writeHeader(HttpHeader.SET_COOKIE, cookieValue);
        writeHeader(HttpHeader.LOCATION, path);
        writeEndOfHeaders();
    }

    public void notFound() throws IOException {
        String errorMessage = "404 Not Found";
        byte[] errorBody = errorMessage.getBytes();
        
        writeStatusLine(HttpStatus.NOT_FOUND);
        writeHeader(HttpHeader.CONTENT_TYPE, ContentType.TEXT_HTML.getValue());
        writeHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(errorBody.length));
        writeEndOfHeaders();
        writeBody(errorBody);
    }

    private void writeStatusLine(HttpStatus status) throws IOException {
        dos.writeBytes(status.getStatusLine(httpVersion) + "\r\n");
    }

    private void writeHeader(HttpHeader header, String value) throws IOException {
        dos.writeBytes(header.getValue() + ": " + value + "\r\n");
    }

    private void writeEndOfHeaders() throws IOException {
        dos.writeBytes("\r\n");
    }

    private void writeBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}