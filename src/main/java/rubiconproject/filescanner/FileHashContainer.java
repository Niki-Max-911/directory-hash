package rubiconproject.filescanner;

import lombok.Data;

import java.nio.file.Path;

/**
 * Container for pair file/directory path and according hash.
 *
 * @author maksimnikitin
 * @since 26.10.17
 */
@Data
public class FileHashContainer implements Comparable<FileHashContainer> {

    private Path path;

    private String hash;

    @Override
    public String toString() {
        return String.join(" ", path.toString(), hash);
    }

    @Override
    public int compareTo(FileHashContainer o) {
        return this.path.toAbsolutePath().compareTo(o.path.toAbsolutePath());
    }
}
