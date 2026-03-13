package ua.university.service;

import ua.university.repository.*;
import ua.university.domain.*;

import java.util.*;


public class FacultyOperations {
    private final Scanner scanner = new Scanner(System.in);
    private final InputValidator inputValidator = new InputValidator();
    private final FacultyRepository facultyRepository;
    private final TeacherRepository teacherRepository;

    public FacultyOperations(FacultyRepository facultyRepository, TeacherRepository teacherRepository) {
        this.facultyRepository = facultyRepository;
        this.teacherRepository = teacherRepository;
    }

    /*
    Displays faculties list
    */
    public void showFaculties(List<Faculty> faculties) {
        if (faculties == null || faculties.isEmpty()) {
            System.out.println("Інформацію про факультети не знайдено");
            return;
        }
        System.out.println("---Список факультетів---");
        for (Faculty faculty : faculties) {
            System.out.println(faculty.toString());
        }
    }

    /**
     * Adds a new faculty to the repository
     */
    public void addFaculty() {
        System.out.println("---Додати новий факультет---");

        int code = facultyRepository.getNextCode();
        System.out.println("Автоматично згенерований код: " + code);

        String name = "";
        do {
            System.out.print("Введіть повну назву факультету: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Назва не може бути порожньою.");
            }
        } while (name.isEmpty());

        String shortName = "";
        do {
            System.out.print("Введіть скорочену назву: ");
            shortName = scanner.nextLine().trim();
            if (shortName.isEmpty()) {
                System.out.println("Скорочена назва не може бути порожньою.");
            }
        } while (shortName.isEmpty());

        String contacts = "";
        do {
            System.out.print("Введіть контакти (email): ");
            contacts = scanner.nextLine().trim();
            contacts = inputValidator.checkEmail(contacts);
            if (contacts.equals("-1")) {
                System.out.println("Невірний email. Приклад: example@ukma.edu.ua");
            }
        } while (contacts.equals("-1"));

        List<Teacher> allTeachers = teacherRepository.findAll();
        Teacher selectedDean = null;

        if (allTeachers.isEmpty()) {
            System.out.println("Немає викладачів. Факультет буде створено без декана.");
        } else {
            System.out.print("\nПризначити декана? (y/n): ");
            String assignDean = scanner.nextLine().trim();

            if (assignDean.equalsIgnoreCase("y")) {
                boolean deanSelected = false;

                while (!deanSelected) {
                    System.out.println("\n=== Оберіть декана зі списку викладачів ===");
                    for (int i = 0; i < allTeachers.size(); i++) {
                        System.out.println((i + 1) + ". [ID: " + allTeachers.get(i).getId() + "] " +
                                allTeachers.get(i).getFullName() + " - " + allTeachers.get(i).getPosition());
                    }
                    System.out.println("0. Пропустити (без декана)");

                    System.out.print("Номер викладача: ");
                    String deanInput = scanner.nextLine().trim();

                    if (deanInput.isEmpty()) {
                        System.err.println("Введення не може бути порожнім!");
                        continue;
                    }

                    try {
                        int deanChoice = Integer.parseInt(deanInput);
                        if (deanChoice == 0) {
                            selectedDean = null;
                            deanSelected = true;
                        } else if (deanChoice >= 1 && deanChoice <= allTeachers.size()) {
                            selectedDean = allTeachers.get(deanChoice - 1);
                            deanSelected = true;
                        } else {
                            System.err.println("Невірний вибір! Оберіть номер від 0 до " + allTeachers.size());
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Невірний формат! Введіть число.");
                    }
                }
            }
        }

        Faculty newFaculty = new Faculty(code, name, shortName, selectedDean, contacts);
        facultyRepository.add(newFaculty);

        System.out.println("Факультет успішно додано до НаУКМА!");
        if (selectedDean != null) {
            System.out.println("   Декан: " + selectedDean.getFullName());
        }
    }


    public void deleteFaculty() {
        System.out.println("---Видалити факультет---");

        List<Faculty> faculties = facultyRepository.findAll();
        if (faculties.isEmpty()) {
            System.err.println("Немає факультетів для видалення!");
            return;
        }

        System.out.println("\n=== Список факультетів ===");
        for (int i = 0; i < faculties.size(); i++) {
            System.out.println((i + 1) + ". [Код: " + faculties.get(i).getCode() + "] " +
                    faculties.get(i).getShortName() + " - " + faculties.get(i).getName());
        }

        System.out.print("\nВведіть код факультету для видалення: ");
        String codeInput = scanner.nextLine().trim();

        if (codeInput.isEmpty()) {
            System.err.println("Код не може бути порожнім!");
            return;
        }

        int code;
        try {
            code = Integer.parseInt(codeInput);
        } catch (NumberFormatException e) {
            System.err.println("Невірний формат!");
            return;
        }

        boolean deleted = facultyRepository.deleteById(code);

        if (deleted) {
            System.out.println("Факультет успішно видалено!");
        } else {
            System.err.println("Факультет не знайдено!");
        }
    }

    public void updateFaculty() {
        System.out.println("---Редагувати факультет---");

        List<Faculty> faculties = facultyRepository.findAll();
        if (faculties.isEmpty()) {
            System.err.println("Немає факультетів для редагування!");
            return;
        }

        System.out.println("\n=== Список факультетів ===");
        for (int i = 0; i < faculties.size(); i++) {
            String deanName = faculties.get(i).getDean() != null ?
                    faculties.get(i).getDean().getFullName() : "не призначено";
            System.out.println((i + 1) + ". [Код: " + faculties.get(i).getCode() + "] " +
                    faculties.get(i).getShortName() + " - " + faculties.get(i).getName() +
                    " (Декан: " + deanName + ")");
        }

        System.out.print("\nВведіть код факультету для редагування: ");
        String codeInput = scanner.nextLine().trim();

        if (codeInput.isEmpty()) {
            System.err.println("Код не може бути порожнім!");
            return;
        }

        int code;
        try {
            code = Integer.parseInt(codeInput);
        } catch (NumberFormatException e) {
            System.err.println("Невірний формат!");
            return;
        }

        Faculty currentFaculty = null;
        for (Faculty f : faculties) {
            if (f.getCode() == code) {
                currentFaculty = f;
                break;
            }
        }

        if (currentFaculty == null) {
            System.err.println("Факультет не знайдено!");
            return;
        }

        System.out.println("\n--- Редагування факультету (введіть 's' щоб залишити поточне значення) ---");

        String newName = "";
        do {
            System.out.print("Повна назва [" + currentFaculty.getName() + "]: ");
            newName = scanner.nextLine().trim();

            if (newName.equalsIgnoreCase("s") || newName.isEmpty()) {
                newName = currentFaculty.getName();
                break;
            }

            if (newName.isEmpty()) {
                System.out.println("Назва не може бути порожньою.");
            }
        } while (newName.isEmpty());

        String newShortName = "";
        do {
            System.out.print("Скорочена назва [" + currentFaculty.getShortName() + "]: ");
            newShortName = scanner.nextLine().trim();

            if (newShortName.equalsIgnoreCase("s") || newShortName.isEmpty()) {
                newShortName = currentFaculty.getShortName();
                break;
            }

            if (newShortName.isEmpty()) {
                System.out.println("Скорочена назва не може бути порожньою.");
            }
        } while (newShortName.isEmpty());

        String newContacts = "";
        do {
            System.out.print("Контакти [" + currentFaculty.getContacts() + "]: ");
            newContacts = scanner.nextLine().trim();

            if (newContacts.equalsIgnoreCase("s") || newContacts.isEmpty()) {
                newContacts = currentFaculty.getContacts();
                break;
            }

            newContacts = inputValidator.checkEmail(newContacts);
            if (newContacts.equals("-1")) {
                System.out.println("Невірний email. Приклад: example@ukma.edu.ua або 's'.");
            }
        } while (newContacts.equals("-1"));

        System.out.println("\nПоточний декан: " +
                (currentFaculty.getDean() != null ? currentFaculty.getDean().getFullName() : "не призначено"));
        System.out.print("Змінити декана? (y/n): ");
        String changeDean = scanner.nextLine().trim();

        Teacher newDean = currentFaculty.getDean();

        if (changeDean.equalsIgnoreCase("y")) {
            List<Teacher> allTeachers = teacherRepository.findAll();
            if (allTeachers.isEmpty()) {
                System.err.println("Немає викладачів!");
            } else {
                boolean deanSelected = false;

                while (!deanSelected) {
                    System.out.println("\n=== Оберіть декана зі списку викладачів ===");
                    for (int i = 0; i < allTeachers.size(); i++) {
                        System.out.println((i + 1) + ". [ID: " + allTeachers.get(i).getId() + "] " +
                                allTeachers.get(i).getFullName() + " - " + allTeachers.get(i).getPosition());
                    }
                    System.out.println("0. Видалити декана (залишити без декана)");

                    System.out.print("Номер викладача: ");
                    String deanInput = scanner.nextLine().trim();

                    if (deanInput.isEmpty()) {
                        System.err.println("Введення не може бути порожнім!");
                        continue;
                    }

                    try {
                        int deanChoice = Integer.parseInt(deanInput);
                        if (deanChoice == 0) {
                            newDean = null;
                            deanSelected = true;
                        } else if (deanChoice >= 1 && deanChoice <= allTeachers.size()) {
                            newDean = allTeachers.get(deanChoice - 1);
                            deanSelected = true;
                        } else {
                            System.err.println("Невірний вибір! Оберіть номер від 0 до " + allTeachers.size());
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Невірний формат! Введіть число.");
                    }
                }
            }
        }

        Faculty updatedFaculty = new Faculty(code, newName, newShortName, newDean, newContacts);

        boolean updated = facultyRepository.update(code, updatedFaculty);

        if (updated) {
            System.out.println("Факультет успішно оновлено!");
            if (newDean != null) {
                System.out.println("   Декан: " + newDean.getFullName());
            }
        } else {
            System.err.println("Помилка при оновленні!");
        }
    }
}
