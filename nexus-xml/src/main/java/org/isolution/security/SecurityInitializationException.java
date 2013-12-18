package org.isolution.security;

/**
 * User: alexwibowo
 */
public class SecurityInitializationException extends SecurityException {

    public SecurityInitializationException(String msg) {
        super(msg);
    }

    public SecurityInitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
