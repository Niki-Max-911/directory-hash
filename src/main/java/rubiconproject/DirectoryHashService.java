package rubiconproject;

import rubiconproject.filescanner.FileHashContainer;
import rubiconproject.filescanner.HashFileVisitorImpl;
import rubiconproject.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: description
 *
 * @author maksimnikitin
 * @since 27.10.17
 */
public class DirectoryHashService {

    public static void performHashCalculating(Path inputPath) throws IOException {
        HashFileVisitorImpl visitor = new HashFileVisitorImpl();
        Files.walkFileTree(inputPath, visitor);

        List<String> resultLines = visitor.getFilesHashSet()
                .stream()
                .map(FileHashContainer::toString)
                .collect(Collectors.toList());

        String outPathStr = System.getProperties().getProperty("rubicon.directoryhash.result.file_path");
        Path outPath = Paths.get(outPathStr).toAbsolutePath();
        FileUtils.writeFile(outPath, resultLines);
    }
}
