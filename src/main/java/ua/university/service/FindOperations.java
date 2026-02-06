package ua.university.service;

import ua.university.domain.Student;
import ua.university.repository.StudentRepository;

import java.util.Scanner;

public class FindOperations {
    /** Adds input validator to the class */
    private final InputValidator inputValidator = new InputValidator();
    /** Adds scanner to the class */
    private final Scanner scanner = new Scanner(System.in);

    public FindOperations () {}

    /**
     * Finds student by their full name
     */
    public void  studentByFullName () {
        String[] names = { "first", "middle", "last" };
        String userInput = "";
        String result = "";

        for (int i=0; i<3; i++) {
            do {
                System.out.print("Write the " + names[i] + " name: ");

                userInput = scanner.next().trim();

                if (inputValidator.isWord(userInput).equals("-1")) System.out.println("Not a word");
            } while (inputValidator.isWord(userInput).equals("-1"));

            result += (userInput + " ");
        }

        String[] resultTokens = result.trim().split(" ");
        StudentRepository studentRepository = new StudentRepository();

        try {
            Student student = studentRepository.FindByFullName(resultTokens)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));

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
                System.out.print("Write the number of the course: ");

                result = Integer.parseInt(scanner.next().trim());

                if (result < 1 || result > 6) throw new IllegalArgumentException();
            } catch (IllegalArgumentException e) {
                System.out.println("Write a natural number from 1 to 6");
            }
        } while (result < 1 || result > 6);

        StudentRepository studentRepository = new StudentRepository();

        int counter = 0;

        for (Student student : studentRepository.FindByCourse(result)) {
            System.out.println(student);
            counter++;
        }

        if (counter == 0) System.err.println("Students not found");
    }

    /**
     * Finds students by their group
     */
    public void studentByGroup () {
        String userInput = "";

        do {
            System.out.print("Write student's group: ");

            userInput = scanner.next().trim();
            userInput = inputValidator.isGroup(userInput);

            if (userInput.equals("-1")) {
                System.out.println("Wrong group. Example '12A'");
            }
        } while (userInput.equals("-1"));

        StudentRepository studentRepository = new StudentRepository();

        int counter = 0;

        for (Student student : studentRepository.FindByGroup(userInput)) {
            System.out.println(student);
            counter++;
        }

        if (counter == 0) System.err.println("Students not found");
    }

    /**
     * Finds student by their student ID
     */
    public void studentByStudentID () {
        String userInput = "";

        do {
            System.out.print("Write student ID: ");

            userInput = scanner.next().trim();
            userInput = inputValidator.isStudentID(userInput);

            if (userInput.equals("-1")) {
                System.out.println("Wrong ID. Example '4BI92H'");
            }
        } while (userInput.equals("-1"));

        StudentRepository studentRepository = new StudentRepository();

        try {
            Student student = studentRepository.FindByStudentID(userInput)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));

            System.out.println(student);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Finds student by their email
     */
    public void studentByEmail () {
        System.out.print("Write student email: ");

        String userInput = scanner.next().trim();

        StudentRepository studentRepository = new StudentRepository();

        try {
            Student student = studentRepository.FindByEmail(userInput)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));

            System.out.println(student);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
