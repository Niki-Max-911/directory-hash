package rubiconproject.utils;

import lombok.extern.java.Log;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class holds hash methods implementation.
 *
 * @author maksimnikitin
 * @since 25.10.17
 */
@Log
public class HashUtils {

    private final static String SHA_512_NAME = MessageDigestAlgorithms.SHA_512;

    /**
     * Method calculates SHA512 hash and returns its in HEX format.
     *
     * @param bytes input bytes for hashing.
     * @return hexed and calculated before SHA512 hash.
     */
    public static String getSha512AsHex(byte[] bytes) {
        return new DigestUtils(SHA_512_NAME)
                .digestAsHex(bytes);
    }

    /**
     * Method calculates hash with SHA512 algorithm and return its in hex form.
     * In case path follows to directory, exception will be thrown.
     *
     * @param path path to target file.
     * @return hexed and calculated before SHA512 hash from target file.
     * @throws IOException when path follows to directory or other exception related with file.
     */
    public static String getSha512FileAsHex(Path path) throws IOException {
        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            log.severe("Hash SHA512 calculation intercepted. Cause is file related exception.");
            log.throwing("HashUtils", "getSha512FileAsHex", e);
            throw e;
        }
        return getSha512AsHex(fileBytes);
    }
}
