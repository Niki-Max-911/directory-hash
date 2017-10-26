package rubiconproject.filescanner;

import rubiconproject.utils.HashUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * TODO: description
 *
 * @author maksimnikitin
 * @since 26.10.17
 */
public class FileVisitorImpl extends SimpleFileVisitor<Path> {


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        String hash = HashUtils.getSha512FileAsHex(file);

        return super.visitFile(file, attrs);
    }
}
