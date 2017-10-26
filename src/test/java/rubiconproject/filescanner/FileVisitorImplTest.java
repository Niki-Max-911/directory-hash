package rubiconproject.filescanner;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import rubiconproject.utils.HashTest;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

/**
 * TODO: description
 *
 * @author maksimnikitin
 * @since 26.10.17
 */
public class FileVisitorImplTest {

    @Test
    public void getFilesHashSet() throws Exception {
        URL inputDirectoryUrl = HashTest.class.getClassLoader().getResource("input");//search in classpath
        Path dirPath = Paths.get(inputDirectoryUrl.toURI());


        HashFileVisitorImpl filesTreeVisitor = new HashFileVisitorImpl();
        Files.walkFileTree(dirPath, filesTreeVisitor);

        TreeSet<FileHashContainer> filesHashSet = filesTreeVisitor.getFilesHashSet();
        filesHashSet.forEach(container -> container.relativizePath(dirPath.getParent()));
        compareHashesWithExpected(filesHashSet);
    }

    @SneakyThrows
    private void compareHashesWithExpected(TreeSet<FileHashContainer> actual) {
        URL expectedResultFileUrl = HashTest.class.getClassLoader().getResource("output/right-results.txt");
        Path expectedResultFilePath = Paths.get(expectedResultFileUrl.toURI());

        Object[] expectedHashes = Files.lines(expectedResultFilePath)
                .map(line -> {
                    String[] lineParts = line.split(" ");
                    Path path = Paths.get(HashTest.class.getClassLoader().getResource(lineParts[0]).toURI());
                    String hash = lineParts[1];
                    return new FileHashContainer(path, hash);
                })
                .map(FileHashContainer::toString)
                .toArray();

        Object[] actualHashes = actual.stream()
                .map(FileHashContainer::toString)
                .toArray();

        //we cant use Collection.containsAll in cause we need save order during comparing
        Assert.assertArrayEquals(expectedHashes, actualHashes);
    }
}