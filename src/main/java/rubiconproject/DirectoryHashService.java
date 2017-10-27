package rubiconproject;

import lombok.extern.java.Log;
import rubiconproject.filescanner.FileHashContainer;
import rubiconproject.filescanner.HashFileVisitorImpl;
import rubiconproject.utils.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service provide methods for calculate hashes for file tree.
 *
 * @author maksimnikitin
 * @since 27.10.17
 */
@Log
public class DirectoryHashService {

    /**
     * Method perform hash calculating for file tree starting from {@param inputPath}.
     * Result stores in run directory in file described in property(rubicon.directoryhash.result.file_path).
     *
     * @param inputPath the starting file.
     */
    public static void performHashCalculating(Path inputPath) {
        HashFileVisitorImpl visitor = new HashFileVisitorImpl();
        try {
            Files.walkFileTree(inputPath, visitor);
        } catch (IOException e) {
            log.severe("Excepting during file tree walking.");
            log.throwing("DirectoryHashService", "performHashCalculating", e);
            return;
        }

        String runDir = System.getProperty("user.dir");
        Path runDirPath = Paths.get(runDir);

        List<String> resultLines = visitor.getFilesHashSet()
                .stream()
                .peek(fileHash->fileHash.relativizePath(runDirPath))//for avoiding absolute path in results
                .map(FileHashContainer::toString)
                .collect(Collectors.toList());

        String outPathStr = System.getProperties().getProperty("rubicon.directoryhash.result.file_path");
        Path outPath = Paths.get(outPathStr).toAbsolutePath();
        try {
            if (!Files.exists(outPath)) {
                Files.createDirectory(outPath.getParent());
            }
            FileUtils.writeFile(outPath, resultLines);
            log.info("Result file have just been created: " + outPath);
        } catch (URISyntaxException | IOException e) {
            log.severe("Result file can't be created.");
        }
    }
}