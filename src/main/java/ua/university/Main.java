package ua.university;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.university.exceptions.FileUpdateFailedException;
import ua.university.exceptions.InitializationFailedException;
import ua.university.network.Client;
import ua.university.repository.*;
import ua.university.service.IOOperations;
import ua.university.ui.InputProcessor;

import java.io.IOException;
import java.nio.file.Path;

public class Main {

    private static final UniversityRepository universityRepository = new UniversityRepository();
    private static final FacultyRepository facultyRepository = new FacultyRepository();
    private static final DepartmentRepository departmentRepository = new DepartmentRepository();
    private static final StudentRepository studentRepository = new StudentRepository();
    private static final TeacherRepository teacherRepository = new TeacherRepository();
    private static final UserRepository userRepository = new UserRepository();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final InputProcessor inputProcessor = new InputProcessor(studentRepository, universityRepository, facultyRepository, departmentRepository, teacherRepository, userRepository);

    public static void main(String[] args) {
        if (!mainInitialization()) return;

        System.out.println();
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   Вас вітає система DigiUni Registry!      ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();

        inputProcessor.defineObject();

        saveChanges();
    }

    /**
     * Initializes data and repositories
     * @return Return false to finish program
     */
    private static boolean mainInitialization() {
        System.out.println("Завантаження бази даних");
        try {
            Client client = new Client();
            client.downloadData();

            System.out.println("Ініціалізація успішна");
        } catch (IOException ex) {
            System.out.println("Не вдалося завантажити базу даних\nЗавершення програми\n");
            return false;
        }

        System.out.println("Ініціалізація репозиторіїв");
        try {
            Initializer.initializeAll(studentRepository, facultyRepository, departmentRepository, teacherRepository, userRepository, Path.of("src", "main", "resources", "data", "running"));
            System.out.println("Ініціалізація успішна\n\n\n");
            logger.info("Repository initialization succeeded");
        } catch (IOException ex) {
            System.out.println("Не вдалося ініціалізувати репозиторії\nЗавершення програми");
            logger.error("Repository initialization failed");
            return false;
        } catch (InitializationFailedException ex) {
            System.out.println("Не вдалося ініціалізувати репозиторії: " + ex.getMessage() + "\n Завершення програми");
            logger.error("Repository initialization failed: {}", ex.getMessage());
            return false;
        }

        return true;
    }

    /** Saves changes to the server */
    private static void saveChanges() {
        System.out.println("\nТриває збереження змін");
        try {
            IOOperations ioOperations = new IOOperations();
            ioOperations.updateRunning(studentRepository, facultyRepository, departmentRepository, teacherRepository);

            Client client = new Client();
            client.saveChanges();
            client.stopServer();

            ioOperations.removeRunning();
        } catch (IOException | FileUpdateFailedException ex) {
            System.out.println("Помилка внесення змін\nВихід без збереження");
            logger.error("Data saving error");
        }
    }
}