package org.isolution.nexus.invoker;

import org.isolution.nexus.domain.EndpointProtocol;
import org.isolution.nexus.xml.SOAPDocument;

import java.io.IOException;
import java.net.URI;

/**
 * A utility class to invoke the specified target &lt;T&gt; with the given
 * {@link SOAPDocument}.
 * Implementor of this interface will need to make sure that the protocol
 * that it claims to support is compatible with the possible values of &lt;T&gt;
 *
 * User: Alex Wibowo
 * Date: 19/04/11
 * Time: 10:19 PM
 */
public interface Invoker<T> {

    /**
     * @param target the endpoint to be invoked.
     * @throws IllegalArgumentException if the target value is incompatible with the supported protocol,
     */
    void setTarget(T target);

    /**
     * @param protocol protocol to be checked against this invoker
     * @return <tt>true</tt> if this invoker supports the specified protocol
     */
    boolean supports(EndpointProtocol protocol);

    /**
     * @param document  {@link SOAPDocument} to be sent to the endpoint
     * @return document the response from the invocation
     * @throws IOException errors that occurred during the invocation
     */
    SOAPDocument invoke(SOAPDocument document)
            throws IOException;
}
