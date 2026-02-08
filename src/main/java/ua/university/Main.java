package ua.university;
import ua.university.repository.StudentRepository;
import ua.university.service.InputProcessor;

public class Main {

    private static final StudentRepository studentRepository = new StudentRepository();

    private static final InputProcessor inputProcessor = new InputProcessor(studentRepository);

    /** Adds a list of access levels */
    private enum AccessLevel {user, manager, admin}
    /** Utilizes the list of access levels */
    private static AccessLevel accessLevel = AccessLevel.manager;

    public static void main(String[] args) {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   Вас вітає система DigiUni Registry!      ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();

        inputProcessor.defineObject();
    }
}