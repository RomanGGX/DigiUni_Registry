package ua.university.service;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

public class IOOperations {

    private final Path running;
    private final Path stable;

    /**
     * Initializes running and stable paths
     * @param running Path that changes during work
     * @param stable Path that changes on save
     * @throws IOException Throws if initialization fails
     */
    public IOOperations(Path running, Path stable) throws IOException {
        running = running.resolve("running");
        stable = stable.resolve("stable");

        if (Files.notExists(running)) {
            Files.createDirectories(running);
            Files.createFile(running.resolve("Faculties.json"));
            Files.createFile(running.resolve("Departments.json"));
            Files.createFile(running.resolve("Students.json"));
            Files.createFile(running.resolve("Teachers.json"));
        }
        if (Files.notExists(stable)) {
            Files.createDirectories(stable);
            Files.createFile(stable.resolve("Faculties.json"));
            Files.createFile(stable.resolve("Departments.json"));
            Files.createFile(stable.resolve("Students.json"));
            Files.createFile(stable.resolve("Teachers.json"));
        }

        this.running = running;
        this.stable = stable;
    }

    public Path getRunning() {return running;}

    public Path getStable() {return stable;}

    /**
     * Transfers all information from src to dst
     * @param src Source path
     * @param dst Destination path
     * @throws IOException Throws if path not found
     */
    public void transferFiles(Path src, Path dst) throws IOException {
        try (Stream<Path> paths = Files.walk(src)) {
            List<Path> validPaths = paths
                    .filter(Files::isRegularFile)
                    .filter(o -> o.getFileName().toString().endsWith(".json"))
                    .toList();
            for (Path p : validPaths) {
                Files.copy(p, dst.resolve(p.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new IOException("Failed to transfer from one file to another", ex);
        }
    }

    /**
     * Copies stable configuration to running configuration
     * @throws IOException Throws if path not found
     */
    public void copyStableToRunning() throws IOException {
        transferFiles(stable, running);
    }
}
