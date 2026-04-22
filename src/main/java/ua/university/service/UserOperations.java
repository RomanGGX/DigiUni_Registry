package ua.university.service;

import ua.university.domain.User;
import ua.university.exceptions.EmptyInputException;
import ua.university.exceptions.UnsupportedIntegerInputException;
import ua.university.repository.UserRepository;

import java.util.Scanner;

public class UserOperations {
    /** Adds scanner to the class */
    private final Scanner scanner = new Scanner(System.in);

    private final UserRepository userRepository;

    public UserOperations(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds user by reading login, password, and access level. Does not add user with the same login name
     */
    public void addUser() {
        if (userRepository.add(new User(readNotEmpty("Введіть логін: "), readNotEmpty("Введіть пароль: "), setAccessLevel())))
            System.out.println("\nКористувача успішно додано");
        else System.out.println("""
                
                Користувача не додано
                Причина - користувач з цим логіном вже існує""");
    }

    /**
     * Reads a not empty line message
     * @param message String message in the same line with the answer
     * @return String user input
     */
    private String readNotEmpty(String message) {
        boolean checker = false;
        String result = "";

        System.out.print(message);

        do {
            try {
                result = scanner.nextLine();

                if (result.isEmpty()) throw new EmptyInputException("Empty login input");

                checker = true;
            } catch (EmptyInputException ex) {
                System.out.println("\nПорожнє введення");
            }
        } while (!checker);

        return result;
    }

    /**
     * Allows the user to choose access level of a new user
     * @return int value from 1 to 3
     */
    private int setAccessLevel() {
        int result = 0;
        boolean resultApproved = false;

        System.out.println("""
                Оберіть рівень доступу:
                1 - користувач
                2 - менеджер
                3 - адміністратор
                """);

        do {
            try {
                String stringResult = scanner.nextLine();

                if (stringResult.isEmpty()) throw new EmptyInputException("Empty input");
                else result = Integer.parseInt(stringResult);

                if (result < 1 || result > 3) throw new UnsupportedIntegerInputException("Unsupported number");
                resultApproved = true;
            } catch (UnsupportedIntegerInputException | NumberFormatException ex) {
                System.out.println("Введіть коректне значення (1-3)");
            } catch (EmptyInputException ex) {
                System.out.println("Порожнє введення");
            }
        } while (!resultApproved);

        switch (result) {
            case 1:
                break;
            case 2:
                result = 0b0011;
                break;
            case 3:
                result = 0b0111;
                break;
        }

        return result;
    }
}
