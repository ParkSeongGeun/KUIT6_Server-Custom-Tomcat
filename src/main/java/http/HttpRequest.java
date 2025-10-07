package http;

import http.enums.HttpMethod;
import http.util.HttpRequestUtils;
import http.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final HttpStartLine startLine;
    private final HttpHeaders headers;
    private final String body;

    private HttpRequest(HttpStartLine startLine, HttpHeaders headers, String body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest from(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("Request line cannot be null or empty");
        }

        HttpStartLine startLine = HttpStartLine.from(requestLine);
        HttpHeaders headers = HttpHeaders.from(br);
        
        String body = null;
        if (startLine.getMethod() == HttpMethod.POST && headers.getContentLength() > 0) {
            body = IOUtils.readData(br, headers.getContentLength());
        }

        return new HttpRequest(startLine, headers, body);
    }

    public HttpMethod getMethod() {
        return startLine.getMethod();
    }

    public String getUrl() {
        return startLine.getPath();
    }

    public String getPath() {
        return startLine.getPathWithoutQuery();
    }

    public String getQueryString() {
        return startLine.getQueryString();
    }

    public String getVersion() {
        return startLine.getVersion();
    }

    public String getHeader(String headerName) {
        return headers.getHeader(headerName);
    }

    public String getCookie() {
        return headers.getCookie();
    }

    public String getCookie(String key) {
        String cookieHeader = headers.getCookie();
        if (cookieHeader == null) {
            return null;
        }

        for (String pair : cookieHeader.split(";\\s*")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) {
                return kv[1];
            }
        }
        return null;
    }

    public int getContentLength() {
        return headers.getContentLength();
    }

    public String getBody() {
        return body;
    }

    public HttpStartLine getStartLine() {
        return startLine;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        if (startLine.getMethod() == HttpMethod.GET && getQueryString() != null) {
            return HttpRequestUtils.parseQueryParameter(getQueryString());
        } else if (startLine.getMethod() == HttpMethod.POST && body != null) {
            return HttpRequestUtils.parseQueryParameter(body);
        }
        return new HashMap<>();
    }
}