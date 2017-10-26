package rubiconproject;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for class {@link FileUtils}.
 *
 * @author maksimnikitin
 * @since 26.10.17
 */
public class FileUtilsTest {

    static final String FILE_NAME = "test-result.dat";

    @Rule
    public TemporaryFolder dirResource = new TemporaryFolder();//auto remove after test

    @Test
    public void writeFile() throws Exception {
        List<String> lines = Arrays.asList("1", "2", "3");

        Path filePath = getFilePath();
        FileUtils.writeFile(filePath, lines);

        Assert.assertTrue(Files.exists(filePath));
        Assert.assertArrayEquals(lines.toArray(), Files.lines(filePath).toArray());
    }

    @Test
    public void createFile() throws Exception {
        Path filePath = getFilePath();
        Path createdFile = FileUtils.createFile(filePath);
        Assert.assertTrue(Files.exists(createdFile));
    }

    @Test
    public void createDuplicateFile() throws Exception {
        Path filePath = getFilePath();
        Path createdFile = FileUtils.createFile(filePath);
        Assert.assertTrue(Files.exists(createdFile));

        //create one more file with the same path
        Path createdFile2 = FileUtils.createFile(filePath);
        Assert.assertTrue(Files.exists(createdFile2));

        Path fileName = createdFile.getFileName();
        Path fileName2 = createdFile2.getFileName();
        Assert.assertTrue(fileName2.toString().endsWith(fileName.toString()));
        Assert.assertNotEquals(fileName.toString(), fileName2.toString());
    }

    private Path getFilePath() {
        Path dirPath = dirResource.getRoot().toPath();
        return Paths.get(dirPath.toString(), FILE_NAME);//join dir path and future filename
    }
}