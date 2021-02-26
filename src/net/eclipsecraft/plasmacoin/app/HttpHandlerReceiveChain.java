package net.eclipsecraft.plasmacoin.app;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.eclipsecraft.plasmacoin.Objects.AddBock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class HttpHandlerReceiveChain implements HttpHandler {
    private static Gson gson = new Gson();
    private PeerToPeerServer p2pServer;

    public HttpHandlerReceiveChain(PeerToPeerServer p2pServer){
        this.p2pServer = p2pServer;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;
        if("POST".equals(httpExchange.getRequestMethod())) {
//            System.out.println("Got Request Message of POST");
            requestParamValue = handlePostRequest(httpExchange);
            handlePostResponse(httpExchange,requestParamValue);
        }
    }
    private String handlePostRequest(HttpExchange httpExchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
        String request = "";
        while(reader.ready()){
            request=request+reader.readLine();
        }
        return gson.fromJson(request, AddBock.class).getData();
    }

    private void handlePostResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        String htmlResponse = "";
        if(requestParamValue==null){
            htmlResponse = "Error";
            httpExchange.sendResponseHeaders(200, htmlResponse.length());
            outputStream.write(htmlResponse.getBytes());
            outputStream.flush();
            outputStream.close();
            return;
        }
        Main.getCurrentChain().addBlock(requestParamValue);
        p2pServer.syncChains();
        String response = gson.toJson(Main.getCurrentChain());
        // this line is a must
        httpExchange.sendResponseHeaders(200, response.length());
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
