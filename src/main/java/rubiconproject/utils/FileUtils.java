package rubiconproject.utils;

import lombok.extern.java.Log;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.List;

/**
 * Utility class holds file related methods implementation.
 *
 * @author maksimnikitin
 * @since 26.10.17
 */
@Log
public class FileUtils {

    /**
     * Method creates file according to path param.
     * Next all asked lines will be written into file.
     * If file exists, new one creates with random digital sequence appends to filename.
     *
     * @param path  asked file path.
     * @param lines target lines to saving.
     */
    public static void writeFile(Path path, List<? extends CharSequence> lines) {
        Path fileResultPath = null;
        try {
            fileResultPath = createFile(path);
            log.info("Result file have just been created: \n" + fileResultPath);
        } catch (URISyntaxException | IOException e) {
            log.severe("Result file can't be created.");
            e.printStackTrace();
        }


        try {
            Files.write(fileResultPath, lines);
        } catch (IOException e) {
            log.severe("Result file can't be written.");
            e.printStackTrace();
        }
    }

    /**
     * Method creates file according to path param.
     * If file exists, new one creates with random digital sequence appends to filename.
     *
     * @return path to created file.
     * @throws URISyntaxException throws if file can't be created because of path reasons.
     */
    static Path createFile(Path resultFilePath) throws URISyntaxException, IOException {
        if (Files.exists(resultFilePath)) {
            String updatedFileName =
                    String.format("%d-%s", System.currentTimeMillis(), resultFilePath.getFileName().toString());
            resultFilePath = resultFilePath.resolveSibling(updatedFileName);
        }
        return Files.createFile(resultFilePath);
    }
}