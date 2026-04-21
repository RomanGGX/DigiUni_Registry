package ua.university.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.exceptions.FileUpdateFailedException;
import ua.university.repository.DepartmentRepository;
import ua.university.repository.FacultyRepository;
import ua.university.repository.StudentRepository;
import ua.university.repository.TeacherRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
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

    /**
     * OVERLOADING. Initializes running and stable paths
     * @throws IOException Throws of initialization fails
     */
    public IOOperations() throws IOException {
        this(Path.of("src", "main", "resources", "data"), Path.of("src", "main", "resources", "data"));
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

    /**
     * Copies running configuration to stable configuration
     * @throws IOException Throws if path not found
     */
    public void copyRunningToStable() throws IOException {
        transferFiles(running, stable);
    }

    /**
     * Saves an information about changes in repositories to running configuration
     * @throws IOException Throws if threads are interrupted
     * @throws FileUpdateFailedException Throws if unable to update file
     */
    public void updateRunning(StudentRepository studentRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository) throws IOException, FileUpdateFailedException {
        ObjectMapper mapper = new ObjectMapper();

        Thread updateStudentsThread = new Thread(() -> {
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(running.resolve("Students.json").toFile(), studentRepository.findAll());
            } catch (IOException e) {
                throw new FileUpdateFailedException("Failed to update students file",e);
            }
        }, "Update students thread");
        Thread updateFacultiesThread = new Thread(() -> {
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(running.resolve("Faculties.json").toFile(), facultyRepository.findAll());
            } catch (IOException e) {
                throw new FileUpdateFailedException("Failed to update faculties file", e);
            }
        }, "Update faculties thread");
        Thread updateDepartmentsThread = new Thread(() -> {
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(running.resolve("Departments.json").toFile(), departmentRepository.findAll());
            } catch (IOException e) {
                throw new FileUpdateFailedException("Failed to update departments file", e);
            }
        }, "Update departments thread");
        Thread updateTeachersThread = new Thread(() -> {
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(running.resolve("Teachers.json").toFile(), teacherRepository.findAll());
            } catch (IOException e) {
                throw new FileUpdateFailedException("Failed to update teachers file", e);
            }
        }, "Update teachers thread");

        updateStudentsThread.start();
        updateFacultiesThread.start();
        updateDepartmentsThread.start();
        updateTeachersThread.start();

        try {
            updateStudentsThread.join();
            updateFacultiesThread.join();
            updateDepartmentsThread.join();
            updateTeachersThread.join();
        } catch (InterruptedException e) {
            throw new IOException("Threads interrupted on saving", e);
        }
    }

    public void removeRunning() throws IOException {
        try (Stream<Path> paths = Files.walk(running)) {
            paths.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::deleteOnExit);
        } catch (IOException e) {
            throw new IOException("Failed to delete running configuration", e);
        }
    }
}
