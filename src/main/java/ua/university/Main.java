package ua.university;
import ua.university.service.InputProcessor;
import ua.university.service.InputValidator;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    /** Adds input scanner to the program */
    private static final Scanner scanner = new Scanner(System.in);
    /** Adds input validator to the program */
    private static final InputValidator inputValidator = new InputValidator();
    /** Adds input processor to the program */
    private static final InputProcessor inputProcessor = new InputProcessor();

    /** Adds a list of access levels */
    private enum AccessLevel {user, manager, admin}
    /** Utilizes the list of access levels */
    private static AccessLevel accessLevel = AccessLevel.user;

    /** Defines if program is running (true by default) */
    protected static boolean programRunning = true;

    public static void main(String[] args) {
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