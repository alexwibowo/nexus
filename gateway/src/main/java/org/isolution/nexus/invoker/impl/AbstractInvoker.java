package org.isolution.nexus.invoker.impl;

import org.isolution.nexus.domain.EndpointProtocol;
import org.isolution.nexus.invoker.Invoker;
import org.isolution.nexus.xml.SOAPDocument;

import java.io.IOException;

/**
 * User: Alex Wibowo
 * Date: 11/07/11
 * Time: 10:19 PM
 */
public abstract class AbstractInvoker implements Invoker {
    @Override
    public void setTarget(Object target) {
        // do nothing
    }

    @Override
    public boolean supports(EndpointProtocol protocol) {
        return false; // by default supports nothing!
    }

    @Override
    public SOAPDocument invoke(SOAPDocument document) throws IOException {
        // do nothing
        return null;
    }
}
