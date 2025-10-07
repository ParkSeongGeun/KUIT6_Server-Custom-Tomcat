package http;

import http.enums.HttpMethod;

public class HttpStartLine {
    private final HttpMethod method;
    private final String path;
    private final String version;
    private final String pathWithoutQuery;
    private final String queryString;

    public HttpStartLine(HttpMethod method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;

        // 생성 시 한 번만 파싱
        int queryIndex = path.indexOf("?");
        if (queryIndex != -1) {
            this.pathWithoutQuery = path.substring(0, queryIndex);
            this.queryString = path.substring(queryIndex + 1);
        } else {
            this.pathWithoutQuery = path;
            this.queryString = null;
        }
    }

    public static HttpStartLine from(String requestLine) {
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("Request line cannot be null or empty");
        }

        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid request line format: " + requestLine);
        }

        HttpMethod method = HttpMethod.from(parts[0]);
        String path = parts[1];
        String version = parts[2];

        return new HttpStartLine(method, path, version);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getPathWithoutQuery() {
        return pathWithoutQuery;
    }

    public String getQueryString() {
        return queryString;
    }
}