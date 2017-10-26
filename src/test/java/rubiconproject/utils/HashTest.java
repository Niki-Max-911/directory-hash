package rubiconproject.utils;


import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class HashTest {

    /**
     * Specifically that SHA-512 hash("hello world") will be 309ecc...
     */
    @Test
    @SneakyThrows
    public void exampleStringHashTest() {
        byte[] inputBytes = "hello world".getBytes("UTF-8");
        String hash = HashUtils.getSha512AsHex(inputBytes);
        Assert.assertTrue(hash.startsWith("309ecc"));
    }

    /**
     * Hash of input/bar/fileA.dat will be af371785c4fe...
     */
    @Test
    @SneakyThrows
    public void exampleFileHashTest() {
        URL fileUrl = HashTest.class.getClassLoader().getResource("input/bar/fileA.dat");//search in classpath
        Path filePath = Paths.get(fileUrl.toURI());
        String hash = HashUtils.getSha512FileAsHex(filePath);
        Assert.assertTrue(hash.startsWith("af371785c4fe"));
    }

    /**
     * hash of input/faz is hash("9dd88c920...40c9964..."), a concatenation of hash(fileD.dat) + hash(fileE.dat)
     */
    @Test
    @SneakyThrows
    public void exampleDirectoryHashTest() {
        URL directoryUrl = HashTest.class.getClassLoader().getResource("input/faz");//search in classpath
        Path directoryPath = Paths.get(directoryUrl.toURI());

        String dirHash = Files.list(directoryPath)//dir calculate
                .sorted()
                .map(path -> {
                    String sha512FileAsHex = null;
                    try {
                        sha512FileAsHex = HashUtils.getSha512FileAsHex(path);
                    } catch (IOException e) {
                        Assert.fail("File exception.");
                    }
                    return sha512FileAsHex;
                })
                .collect(Collectors.joining());

        Assert.assertTrue(dirHash.matches("9dd88c920.+?40c9964.*"));
    }

    @Test(expected = IOException.class)
    @SneakyThrows
    public void exampleFileHashFromDirectoryTest() {
        URL fileUrl = HashTest.class.getClassLoader().getResource("input/bar");//search in classpath
        Path dirPath = Paths.get(fileUrl.toURI());
        HashUtils.getSha512FileAsHex(dirPath);
        Assert.fail();
    }
}
