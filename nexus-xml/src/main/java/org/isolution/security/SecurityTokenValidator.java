package org.isolution.security;

import java.security.MessageDigest;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import static java.lang.String.format;

/**
 * User: alexwibowo
 */
public class SecurityTokenValidator {

    public SecurityHelper helper;

    private String secret;

    private MessageDigest messageDigest;

    /**
     * Create security token validator instance.
     * <p/>
     * @param helper instance of {@link SecurityHelper}
     * @param secret secret key to be used to produce the digest
     * @throws SecurityException on failure to get message digest provider
     */
    public SecurityTokenValidator(SecurityHelper helper, String secret)
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
     * Verify the message digest in {@link org.isolution.security.SecurityToken#messageDigest}, using
     * <ul>
     *     <li>{@link org.isolution.security.SecurityToken#timestamp}</li>
     *     <li>{@link org.isolution.security.SecurityToken#nonce}</li>
     *     <li>Agreed shared key</li>
     * </ul>
     *
     *
     * @param digest digest to verify
     * @param ttlInSeconds represents the number of seconds the token is valid until (since the date it was created)
     * @throws org.isolution.security.SecurityException on failure to verify the digest
     */
    public void verify(SecurityToken digest, long ttlInSeconds)
            throws SecurityException {
        if (digest.getTimestamp() == null || "".equals(digest.getTimestamp().trim())) {
            throw new IllegalArgumentException("Timestamp must be provided");
        }
        if (digest.getNonce() == null || "".equals(digest.getNonce().trim())) {
            throw new IllegalArgumentException("Nonce key must be provided");
        }
        verifyTimestampFreshness(digest, ttlInSeconds);
        verifyMessageDigest(digest);
    }


    private void verifyMessageDigest(SecurityToken digest){
        byte[] b4 = helper.appendAll(Base64.decode(digest.getNonce()), digest.getTimestamp().getBytes(), secret.getBytes());
        messageDigest.reset();
        messageDigest.update(b4);
        byte[] checkBytes = messageDigest.digest();

        byte[] decodedMessageDigest = Base64.decode(digest.getMessageDigest());
        if (!Arrays.equals(decodedMessageDigest, checkBytes)) {
            throw new SecurityException("Message digest verification failed.");
        }
    }

    private void verifyTimestampFreshness(SecurityToken digest, long ttlInSeconds) throws SecurityException {
        try {
            long ttlInMilliseconds = ttlInSeconds * 1000;

            String timestamp = digest.getTimestamp();
            Date now = new Date();
            Date date = new SecurityHelper().parseTimestamp(timestamp);

            long nowMillis = now.getTime();
            long timestampMillis = date.getTime();

            if ((nowMillis - timestampMillis) > ttlInMilliseconds) {
                throw new SecurityException(format("Timestamp is older than %d seconds", ttlInSeconds));
            }
        } catch (ParseException e) {
            throw new SecurityException("Unable to parse timestamp");
        }
    }
}
