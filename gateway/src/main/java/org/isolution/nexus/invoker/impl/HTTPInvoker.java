package org.isolution.nexus.invoker.impl;

import org.apache.log4j.Logger;
import org.isolution.nexus.invoker.InvocationException;
import org.isolution.nexus.invoker.Invoker;
import org.isolution.nexus.xml.SOAPDocument;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * User: agwibowo
 * Date: 19/04/11
 * Time: 10:20 PM
 */
@Component
@Qualifier("http")
public class HTTPInvoker implements Invoker {
    public static final Logger LOGGER = org.apache.log4j.Logger.getLogger(HTTPInvoker.class);

    private final URI uri;

    public HTTPInvoker(String urlString) {
        checkArgument(!isNullOrEmpty(urlString), "URL must not be blank.");
        try {
            URL url = new URL(urlString);
            this.uri = url.toURI();
        } catch (Exception e) {
            String message = String.format("URL [%1s] is not supported, and cant be converted to URI", urlString);
            LOGGER.error(message, e);
            throw new IllegalArgumentException(message, e);
        }
    }

    @Override
    public void invoke(SOAPDocument document)
            throws IOException {
        WebServiceConnection connection = null;
        try {
            connection = getConnection();
            connection.send(document.getRawSoapMessage());
            if (connection.hasError()) {
                throw new InvocationException(connection.getErrorMessage());
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private WebServiceConnection getConnection()
            throws IOException {
        CommonsHttpMessageSender sender = new CommonsHttpMessageSender();
        return sender.createConnection(uri);
    }
}
