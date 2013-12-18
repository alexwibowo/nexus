package org.isolution.security;

import java.security.MessageDigest;

/**
 * User: alexwibowo
 */
public class SecurityTokenGenerator {

    public SecurityHelper helper;

    private String secret;

    private MessageDigest messageDigest;


    /**
     * Create security token generator instance.
     * <p/>
     * @param helper instance of {@link SecurityHelper}
     * @param secret secret key to be used to produce the digest
     * @throws SecurityException on failure to get message digest provider
     */
    public SecurityTokenGenerator(SecurityHelper helper, String secret)
            throws SecurityException {
        try {
            this.secret = secret;
            this.helper = helper;
            // prepare the construct the MessageDigest class
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            throw new SecurityInitializationException("Unable to obtain SHA-1 implementation.", e);
        }
    }

    /**
     * @return a {@link SecurityToken} containing the nonce key, timestamp and the message digest itself.
     * @throws SecurityException on failure to produce the digest
     */
    public SecurityToken generate() {
        try {
            String timestamp = helper.getTimestamp();
            String nonce = helper.getNonce();
            byte[] b4 = helper.appendAll(Base64.decode(nonce), timestamp.getBytes(), secret.getBytes());

            //setup the input of the MessageDigest.
            messageDigest.reset();
            messageDigest.update(b4);
            String digest = Base64.encodeToString(messageDigest.digest(), false);

            return new SecurityToken(timestamp, nonce, digest);
        } catch (Exception e) {
            throw new SecurityException("Unable to generate digest",e);
        }
    }
}