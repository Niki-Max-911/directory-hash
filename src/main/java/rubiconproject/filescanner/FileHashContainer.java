package rubiconproject.filescanner;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;

/**
 * Container for pair file/directory path and according hash.
 *
 * @author maksimnikitin
 * @since 26.10.17
 */
@Data
@AllArgsConstructor
public class FileHashContainer implements Comparable<FileHashContainer> {

    private Path path;

    private String hash;

    @Override
    public String toString() {
        return String.join(" ", path.toString(), hash);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FileHashContainer)) {
            return false;
        }
        FileHashContainer inpContainer = (FileHashContainer) obj;
        return path.equals(inpContainer.path) && hash.equals(inpContainer.hash);
    }

    /**
     * Perform relativize between {@param currentDir} and held files/dirs.
     * Result path will be relatively to input dir.
     * This method uses for avoid absolute path in results.
     *
     * @param currentDir current dir path
     */
    public void relativizePath(Path currentDir) {
        this.path = currentDir.relativize(path);
    }

    @Override
    public int compareTo(FileHashContainer o) {
        return this.path.toAbsolutePath().compareTo(o.path.toAbsolutePath());
    }
}
