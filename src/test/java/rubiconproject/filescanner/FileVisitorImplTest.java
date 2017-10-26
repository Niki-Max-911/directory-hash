package rubiconproject.filescanner;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import rubiconproject.utils.HashUtilsTest;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

/**
 * Final test for classpath://input with known result stored in classpath://output/right-results.txt.
 * Folder scan test.
 *
 * @author maksimnikitin
 * @since 26.10.17
 */
public class FileVisitorImplTest {

    @Test
    @SneakyThrows
    public void getFolderHashes() {
        URL inputDirectoryUrl = HashUtilsTest.class.getClassLoader().getResource("input");//search in classpath
        Path dirPath = Paths.get(inputDirectoryUrl.toURI());

        HashFileVisitorImpl filesTreeVisitor = new HashFileVisitorImpl();
        Files.walkFileTree(dirPath, filesTreeVisitor);

        TreeSet<FileHashContainer> filesHashSet = filesTreeVisitor.getFilesHashSet();
        filesHashSet.forEach(container -> container.relativizePath(dirPath.getParent()));
        compareHashesWithExpected(filesHashSet);
    }

    @SneakyThrows
    private void compareHashesWithExpected(TreeSet<FileHashContainer> actual) {
        URL expectedResultFileUrl = HashUtilsTest.class.getClassLoader().getResource("output/right-results.txt");
        Path expectedResultFilePath = Paths.get(expectedResultFileUrl.toURI());

        Object[] expectedHashes = Files.lines(expectedResultFilePath)
                .toArray();

        Object[] actualHashes = actual.stream()
                .map(FileHashContainer::toString)
                .toArray();

        //we cant use Collection.containsAll because we need save order during comparing
        Assert.assertArrayEquals(expectedHashes, actualHashes);
    }
}