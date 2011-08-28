package org.isolution.nexus.invoker;

import org.springframework.ws.WebServiceException;

/**
 * User: Alex Wibowo
 * Date: 19/04/11
 * Time: 11:24 PM
 */
public class InvocationException extends WebServiceException {

    public InvocationException(String errorMessage) {
        super(errorMessage);
    }

    public InvocationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
