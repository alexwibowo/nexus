package org.isolution.nexus.test.support;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.isolution.nexus.xml.soap.SOAPMessageSuite;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Alex Wibowo
 * Date: 17/08/11
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class EchoHttpServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        String response = SOAPMessageSuite.SOAP11ResponseFixture.getSOAPStr();
        createServer(9000, response, "/echo");

        Thread.sleep(50000);
    }

    public static HttpServer createServer(final int port, final String staticResponse, final String contextPath) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 9);
        HttpContext context = httpServer.createContext(contextPath);
        context.setHandler(new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("---------------- Receiving request ----------------------------");
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, staticResponse.length());

                OutputStream responseBody = httpExchange.getResponseBody();
                ((OutputStream) responseBody).write(staticResponse.getBytes());
                responseBody.flush();
                responseBody.close();
            }
        });
        httpServer.start();
        System.out.println("-------------------- SERVER STARTED ----------------------------");
        return httpServer;
    }
}
