package org.example;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        startServer();
    }

    public static void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/sum", new SumHandler());
        server.start();
        System.out.println("Server started on port 8080...");
    }

    static class SumHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                if (query != null) {
                    String[] params = query.split("&");
                    int sum = 0;
                    for (String param : params) {
                        String[] pair = param.split("=");
                        if (pair.length == 2 && pair[0].equals("num")) {
                            try {
                                sum += Integer.parseInt(pair[1]);
                            } catch (NumberFormatException e) {
                                // Ignore invalid input
                            }
                        }
                    }
                    String response = "Sum: " + sum;
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(400, 0);
                    exchange.getResponseBody().close();
                }
            } else {
                exchange.sendResponseHeaders(405, 0); // Method Not Allowed
                exchange.getResponseBody().close();
            }
        }
    }
}
