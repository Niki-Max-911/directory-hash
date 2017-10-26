package rubiconproject.filescanner;

import lombok.Getter;
import rubiconproject.utils.HashUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Walks a file tree and calculate hash for files and directories.
 * Use this type instance in {@link java.nio.file.Files#walkFileTree} to visit each file in a file tree.
 * After walking result will be available in {@link HashFileVisitorImpl#getFilesHashSet}.
 *
 * @author maksimnikitin
 * @since 26.10.17
 */
public class HashFileVisitorImpl extends SimpleFileVisitor<Path> {

    @Getter
    private TreeSet<FileHashContainer> filesHashSet = new TreeSet<>();

    //hold all hashes in all opened dirs order by filename
    private TreeMap<Path, TreeSet<FileHashContainer>> openDirectoriesHash = new TreeMap<>();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        openDirectoriesHash.put(dir, new TreeSet<FileHashContainer>());
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String hash = HashUtils.getSha512FileAsHex(file);
        TreeSet<FileHashContainer> dirHashes = openDirectoriesHash.get(file.getParent());
        dirHashes.add(new FileHashContainer(file, hash));
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        TreeSet<FileHashContainer> currentDirHashes = openDirectoriesHash.remove(dir);

        //invoke after all entries in current directory is visited
        String joinedHashes = currentDirHashes.stream()
                .map(FileHashContainer::getHash)
                .collect(Collectors.joining());
        String dirHashResult = HashUtils.getSha512AsHex(joinedHashes.getBytes("UTF-8"));

        filesHashSet.addAll(currentDirHashes);

        //if visitor arrives to starting dir
        if (openDirectoriesHash.containsKey(dir.getParent())) {
            //add current dir hash to parent dir hashes list
            TreeSet<FileHashContainer> parentDirHashes = openDirectoriesHash.get(dir.getParent());
            parentDirHashes.add(new FileHashContainer(dir, dirHashResult));
        } else {
            //put root dir hash to final list
            filesHashSet.add(new FileHashContainer(dir, dirHashResult));
        }

        return super.postVisitDirectory(dir, exc);
    }

    /**
     * Perform visiting files/dirs in a file tree and calculate its hashes.
     * Method uses {@link HashFileVisitorImpl}.
     *
     * @param start the starting file path.
     * @return list of containers with path and according hash.
     * @throws IOException exception during walking.
     */
    public static TreeSet<FileHashContainer> scanFileTree(Path start) throws IOException {
        HashFileVisitorImpl visitor = new HashFileVisitorImpl();
        Files.walkFileTree(start, visitor);
        return visitor.getFilesHashSet();
    }
}
