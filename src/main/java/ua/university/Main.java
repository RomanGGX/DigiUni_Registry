package ua.university;
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

    private static final InputProcessor inputProcessor = new InputProcessor(studentRepository, universityRepository, facultyRepository, departmentRepository, teacherRepository, userRepository);

    /** Adds a list of access levels */
    private enum AccessLevel {user, manager, admin}

    public static void main(String[] args) {
        //Initializer.initializeAll(studentRepository, facultyRepository, departmentRepository, teacherRepository, userRepository);

        Path dataPath = Path.of("src", "main", "resources", "data");
        System.out.println("Ініціалізація шляху до бази даних");
        try {
            IOOperations ioOperations = new IOOperations(dataPath, dataPath);
            ioOperations.copyStableToRunning();
            System.out.println("Інціалізація успішна");
        } catch (IOException ex) {
            System.out.println("Не вдалося ініціалізувати шлях до бази даних\nЗавершення програми");
            return;
        }

        System.out.println("Ініціалізація репозиторіїв");
        try {
            Initializer.initializeAll(studentRepository, facultyRepository, departmentRepository, teacherRepository, userRepository, dataPath);
            System.out.println("Ініціалізація успішна\n\n\n");
        } catch (IOException ex) {
            System.out.println("Не вдалося ініціалізувати репозиторії\nЗавершення програми");
            return;
        }

        System.out.println();
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   Вас вітає система DigiUni Registry!      ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();

        inputProcessor.defineObject();
    }
}