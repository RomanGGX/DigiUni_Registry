package ua.university.service;

import ua.university.domain.University;
import ua.university.repository.*;

import java.util.Scanner;

public class UniversityOperations {
    private final Scanner scanner = new Scanner(System.in);
    private final InputValidator inputValidator = new InputValidator();
    private final UniversityRepository universityRepository;

    public UniversityOperations(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    /*
    Displays universities list
    */
    public void showUniversity() {
        University university = universityRepository.getUniversity();
        System.out.println("---Інформація про університет---");
        System.out.println(university.toString());
    }


    public void updateUniversity() {
        System.out.println("---Редагувати інформацію про університет---");

        University currentUniversity = universityRepository.getUniversity();

        System.out.println("\nПоточна інформація:");
        System.out.println(currentUniversity.toString());

        System.out.println("\n--- Введіть нові дані (введіть 's' щоб залишити поточне значення) ---");

        String newFullName = "";
        do {
            System.out.print("Повна назва [" + currentUniversity.fullName() + "]: ");
            newFullName = scanner.nextLine().trim();

            if (newFullName.equalsIgnoreCase("s") || newFullName.isEmpty()) {
                newFullName = currentUniversity.fullName();
                break;
            }

            if (inputValidator.checkWord(newFullName).equals("-1")) {
                System.out.println("Невірна назва. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newFullName).equals("-1"));

        String newShortName = "";
        do {
            System.out.print("Скорочена назва [" + currentUniversity.shortName() + "]: ");
            newShortName = scanner.nextLine().trim();

            if (newShortName.equalsIgnoreCase("s") || newShortName.isEmpty()) {
                newShortName = currentUniversity.shortName();
                break;
            }

            if (newShortName.isEmpty()) {
                System.out.println("Скорочена назва не може бути порожньою.");
            }
        } while (newShortName.isEmpty());

        String newCity = "";
        do {
            System.out.print("Місто [" + currentUniversity.city() + "]: ");
            newCity = scanner.nextLine().trim();

            if (newCity.equalsIgnoreCase("s") || newCity.isEmpty()) {
                newCity = currentUniversity.city();
                break;
            }

            if (inputValidator.checkWord(newCity).equals("-1")) {
                System.out.println("Невірне місто. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newCity).equals("-1"));

        String newAddress = "";
        do {
            System.out.print("Адреса [" + currentUniversity.address() + "]: ");
            newAddress = scanner.nextLine().trim();

            if (newAddress.equalsIgnoreCase("s") || newAddress.isEmpty()) {
                newAddress = currentUniversity.address();
                break;
            }

            if (newAddress.isEmpty()) {
                System.out.println("Адреса не може бути порожньою.");
            }
        } while (newAddress.isEmpty());

        University updatedUniversity = new University(newFullName, newShortName, newCity, newAddress);
        universityRepository.updateUniversity(updatedUniversity);

        System.out.println("Інформацію про університет успішно оновлено!");
    }
}
