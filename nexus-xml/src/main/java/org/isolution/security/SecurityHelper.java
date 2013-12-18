package org.isolution.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * User: alexwibowo
 */
public class SecurityHelper {
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * A cached pseudo-random number generator
     * NB. On some JVMs, caching this random number
     * generator is required to overcome punitive
     * overhead.
     */
    static SecureRandom random = null;

    //Why is 64 bits sufficient? Let me lay out the kind of reasoning you can use to answer this question.
    // I'll assume this is a single-use time-limited URL; after it is used once, it is no longer valid, and after a little while (3 days, say),
    // it expires and is no longer valid. Since the nonce is only meaningful to the server, the only way that an attacker can try a guess is
    // to send the 64-bit guess to the server and see how the server responds. How many guesses can the attacker try in the time before the nonce expires?
    // Let's say the attacker can make 1000 HTTP requests per second (that's a pretty beefy attacker);
    // then the attacker can make about 1000*3600*24*3 = 228 guesses within a 3-day period.
    // Each guess has a 1/264 chance of being right. Therefore, the attacker has at most a 1/236 chance of breaking the scheme.
    // That should be more than secure enough for most settings
    static final int NONCE_LENGTH = 64;

    static{
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(System.nanoTime());
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * @return current timestamp, in the format specified by {@link #TIMESTAMP_FORMAT}
     */
    public String getTimestamp() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(TIMESTAMP_FORMAT);

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Z"));
        cal.setTimeInMillis(System.currentTimeMillis());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Z"));

        return dateFormatter.format(cal.getTime());
    }

    /**
     * @param timestamp timestamp to be parsed
     * @return Date representation of the timestamp
     * @throws java.text.ParseException on failure to parse the timestamp
     */
    public Date parseTimestamp(String timestamp)
            throws ParseException {
        return new SimpleDateFormat(TIMESTAMP_FORMAT).parse(timestamp);
    }

    /**
     * Generate a nonce of the given length using the SHA1PRNG algorithm. The SecureRandom
     * instance that backs this method is cached for efficiency.
     * <p/>
     * @return a nonce of the length specified in {@see #NONCE_LENGTH}
     */
    public String getNonce() {
        return Base64.encodeToString(doGenerateNonce(), false);
    }

    private byte[] doGenerateNonce() {
        byte[] nonce = new byte[NONCE_LENGTH];
        random.nextBytes(nonce);
        return nonce;
    }

    public byte[] appendAll(byte b1[], byte b2[], byte b3[]) {
        byte b4[] = new byte[b1.length + b2.length + b3.length];
        System.arraycopy(b1, 0, b4, 0, b1.length);
        System.arraycopy(b2, 0, b4, b1.length, b2.length);
        System.arraycopy(b3, 0, b4, b1.length + b2.length, b3.length);
        return b4;
    }
}