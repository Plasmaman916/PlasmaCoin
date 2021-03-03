package net.eclipsecraft.plasmacoin.app;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class GetTransactions implements HttpHandler {
    private static Gson gson = new Gson();
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;
        if("GET".equals(httpExchange.getRequestMethod())) {
            System.out.println("Got Request Message of GET");
            requestParamValue = handleGetRequest(httpExchange);
            handleGetResponse(httpExchange,requestParamValue);
        }
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        return httpExchange.getRequestURI()
                .toString();
//                .split("\\?")[1]
//                .split("=")[1];
    }

    private void handleGetResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        System.out.println("Handling Reponse");
        String response = gson.toJson(Main.getPool());
        String htmlResponse = response;
        // this line is a must
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
