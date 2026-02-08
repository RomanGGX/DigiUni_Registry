package ua.university;
import ua.university.repository.StudentRepository;
import ua.university.service.InputProcessor;
import ua.university.service.InputValidator;

import java.util.Scanner;

public class Main {
    /** Adds input scanner to the program */
    private static final Scanner scanner = new Scanner(System.in);
    /** Adds input validator to the program */
    private static final InputValidator inputValidator = new InputValidator();

    private static final StudentRepository studentRepository = new StudentRepository();

    private static final InputProcessor inputProcessor = new InputProcessor(studentRepository);

    /** Adds a list of access levels */
    private enum AccessLevel {user, manager, admin}
    /** Utilizes the list of access levels */
    private static AccessLevel accessLevel = AccessLevel.manager;
    /** Defines if program is running (true by default) */
    protected static boolean programRunning = true;

    public static void main(String[] args) {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   Вас вітає система DigiUni Registry!      ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();

        inputProcessor.defineObject();
    }
}