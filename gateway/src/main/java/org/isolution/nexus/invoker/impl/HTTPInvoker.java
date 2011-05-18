package org.isolution.nexus.invoker.impl;

import org.apache.log4j.Logger;
import org.isolution.nexus.domain.EndpointProtocol;
import org.isolution.nexus.invoker.InvocationException;
import org.isolution.nexus.invoker.Invoker;
import org.isolution.nexus.xml.SOAPDocument;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: agwibowo
 * Date: 19/04/11
 * Time: 10:20 PM
 */
@Component
@Qualifier("http")
public class HTTPInvoker implements Invoker<URI> {
    public static final Logger LOGGER = org.apache.log4j.Logger.getLogger(HTTPInvoker.class);

    private URI uri;

    private final EndpointProtocol supportedProtocol = EndpointProtocol.HTTP;

    @Override
    public boolean supports(EndpointProtocol protocol) {
        return protocol.equals(supportedProtocol);
    }

    @Override
    public void setTarget(URI target) {
        checkNotNull(target);
        this.uri = target;
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
