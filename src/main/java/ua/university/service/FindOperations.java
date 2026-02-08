package ua.university.service;

import ua.university.domain.Student;
import ua.university.repository.StudentRepository;

import java.util.Scanner;

public class FindOperations {
    /** Adds input validator to the class */
    private final InputValidator inputValidator = new InputValidator();
    /** Adds scanner to the class */
    private final Scanner scanner = new Scanner(System.in);
    private final StudentRepository studentRepository;

    public FindOperations (StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Finds student by their full name
     */
    public void  studentByFullName () {
        String[] names = { "ім'я", "по батькові", "прізвище" };
        String userInput;
        String result = "";

        for (int i=0; i<3; i++) {
            do {
                System.out.print("Введіть " + names[i] + ": ");

                userInput = scanner.nextLine().trim();

                if (inputValidator.checkWord(userInput).equals("-1")) System.out.println("Ввід не є словом");
            } while (inputValidator.checkWord(userInput).equals("-1"));

            result += (userInput + " ");
        }

        String[] resultTokens = result.trim().split(" ");

        try {
            Student student = studentRepository.FindByFullName(resultTokens)
                    .orElseThrow(() -> new IllegalArgumentException("Студента не знайдено"));

            System.out.println(student);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Finds students by their course
     */
    public void studentByCourse () {
        int result = 0;

        do {
            try {
                System.out.print("Введіть номер курсу: ");

                result = Integer.parseInt(scanner.nextLine().trim());

                if (result < 1 || result > 6) throw new IllegalArgumentException();
            } catch (IllegalArgumentException e) {
                System.out.println("Введіть натуральне число від 1 до 6");
            }
        } while (result < 1 || result > 6);

        int counter = 0;

        for (Student student : studentRepository.FindByCourse(result)) {
            System.out.println(student);
            counter++;
        }

        if (counter == 0) System.err.println("Студента не знайдено");
    }

    /**
     * Finds students by their group
     */
    public void studentByGroup () {
        String userInput;

        do {
            System.out.print("Введіть номер групи: ");

            userInput = scanner.nextLine().trim();
            userInput = inputValidator.checkGroup(userInput);

            if (userInput.equals("-1")) {
                System.out.println("Неправильний запис. Приклад '12A'");
            }
        } while (userInput.equals("-1"));

        int counter = 0;

        for (Student student : studentRepository.FindByGroup(userInput)) {
            System.out.println(student);
            counter++;
        }

        if (counter == 0) System.err.println("Студента не знайдено");
    }

    /**
     * Finds student by their student ID
     */
    public void studentByStudentID () {
        String userInput;

        do {
            System.out.print("Введіть ID студента: ");

            userInput = scanner.nextLine().trim();
            userInput = inputValidator.checkStudentID(userInput);

            if (userInput.equals("-1")) {
                System.out.println("Неправильний запис. Приклад '4BI92H'");
            }
        } while (userInput.equals("-1"));

        try {
            Student student = studentRepository.FindByStudentID(userInput)
                    .orElseThrow(() -> new IllegalArgumentException("Студента не знайдено"));

            System.out.println(student);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Finds student by their email
     */
    public void studentByEmail () {

        String userInput;

        do {
            System.out.print("Введіть email: ");

            userInput = scanner.nextLine().trim();
            userInput = inputValidator.checkEmail(userInput);

            if (userInput.equals("-1")) {
                System.out.println("Неправильний запис. Приклад 'example@gmail.com'");
            }
        } while (userInput.equals("-1"));

        try {
            Student student = studentRepository.FindByEmail(userInput)
                    .orElseThrow(() -> new IllegalArgumentException("Студента не знайдено"));

            System.out.println(student);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}