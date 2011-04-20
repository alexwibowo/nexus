package org.isolution.nexus.invoker;

import org.isolution.nexus.xml.SOAPDocument;

import java.io.IOException;

/**
 * User: agwibowo
 * Date: 19/04/11
 * Time: 10:19 PM
 */
public interface Invoker {

    void invoke(SOAPDocument document)
            throws IOException;
}
