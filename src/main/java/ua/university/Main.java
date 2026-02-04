package ua.university;
import ua.university.validations.InputValidator;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    /** Adds input scanner to the program */
    private static final Scanner scanner = new Scanner(System.in);
    /** Adds input validator to the program */
    private static final InputValidator inputValidator = new InputValidator();

    /** Adds a list of access levels */
    private enum AccessLevel {user, manager, admin}
    /** Utilizes the list of access levels */
    private static AccessLevel accessLevel = AccessLevel.user;

    /** Defines if program is running (true by default) */
    private static boolean programRunning = true;

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
        String[] userTokens = userInput.split(" ");

        // Catches the command if it is not listed
        try {
            for (int i = 0; i < userTokens.length; i++) {
                if (!inputValidator.commandValidator(userTokens[i], access, i))
                    throw new IllegalArgumentException("Command not found. Word number: " + (i+1));
            }
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}