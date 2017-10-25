package rubiconproject;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Utility class that hold hash method implementation.
 *
 * @author maksimnikitin
 * @since 25.10.17
 */
public class HashUtils {

    private final static String SHA_512_NAME = "SHA_512";

    /**
     * Method calculates SHA512 hash and returns its in HEX format.
     *
     * @param bytes input bytes for hashing
     * @return hexed and calculated before SHA512 hash
     */
    public String getSha512AsHex(byte[] bytes) {
        return new DigestUtils(SHA_512_NAME)
                .digestAsHex(bytes);
    }
}
