package org.isolution.security;

/**
 * User: alexwibowo
 */
public class SecurityToken {

    private String timestamp;

    private String nonce;

    private String messageDigest;

    public SecurityToken(String timestamp, String nonce, String messageDigest) {
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.messageDigest = messageDigest;
    }

    /**
     * @return timestamp used when producing the message digest
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @return nonce key used when producing the message digest
     */
    public String getNonce() {
        return nonce;
    }

    /**
     * @return the message digest
     */
    public String getMessageDigest() {
        return messageDigest;
    }

}
