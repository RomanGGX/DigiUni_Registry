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

        System.out.println(" 1. show student - Показати список студентів");
        System.out.println(" 2. find student [by id, full_name] - Знайти студента");
        System.out.println(" 3. add student     - Додати студента");
        System.out.println(" 4. update student  - Оновити інформацію існуючого студента");
        System.out.println(" 5. delete student  - Видалити студента");
        System.out.println();

        while (programRunning) {
            // Defines user access before every command
            switch (accessLevel){
                case user:
                    operateRequest("> ", 0);
                    break;
                case manager:
                    operateRequest("# ", 1);
                    break;
                case admin:
                    operateRequest("admin# ", 2);
            }
        }
    }
    /**
     * Makes program operate a request
     * @param lineStart String that starts every line
     * @param access int that defines access level of user
     */
    private static void operateRequest(String lineStart, int access) {
        System.out.print(lineStart);
        String userInput = scanner.nextLine().trim().toLowerCase();

        if (userInput.isEmpty()) {
            System.out.println("Empty input");
            return;
        }

        String[] userTokens = userInput.split(" ");

        int[] commandCode = inputValidator.commandValidator(userTokens, userTokens.length);

        for (int i=0; i<commandCode.length; i++) {
            if (commandCode[i] == -1) {
                System.err.println("Command was not found. Word number: " + (i+1));
                return;
            }
        }

        inputProcessor.defineProcess(commandCode, access);
    }
}