package webserver;

import http.HttpRequest;
import http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler implements Runnable {
    Socket connection;
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.log(Level.INFO, "New Client Connect! Connected IP : " + connection.getInetAddress() + ", Port : " + connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            HttpRequest httpRequest = HttpRequest.from(br);
            HttpResponse httpResponse = new HttpResponse(out, httpRequest.getVersion());

            RequestMapper requestMapper = new RequestMapper();
            requestMapper.getController(httpRequest.getPath(), httpRequest.getMethod())
                    .execute(httpRequest, httpResponse);

        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
    }
}