package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Teacher;
import ua.university.repository.*;

import java.util.Scanner;

public class DepartmentOperations {
    private final Scanner scanner = new Scanner(System.in);
    private final InputValidator inputValidator = new InputValidator();
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;

    public DepartmentOperations(FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository) {
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
    }

    /*
    Displays departments list
     */
    public void showDepartments(Department[] departments) {
        if (departments == null || departments.length == 0) {
            System.out.println("Немає кафедр");
            return;
        }
        System.out.println("---Список кафедр---");
        for (Department department : departments) {
            String facultyName = department.getFaculty() != null ?
                    department.getFaculty().getShortName() : "немає";
            String headName = department.getHead() != null ?
                    department.getHead().getLastName() : "не призначено";

            System.out.println("Department{" +
                    "code=" + department.getCode() +
                    ", name='" + department.getName() + '\'' +
                    ", faculty='" + facultyName + '\'' +
                    ", head='" + headName + '\'' +
                    ", cabinet='" + department.getCabinet() + '\'' +
                    '}');
        }
    }

    /**
     * Adds a new department to the repository
     */
    public void addDepartment() {
        System.out.println("---Додати нову кафедру---");

        Faculty[] faculties = facultyRepository.getFaculties();
        if (faculties.length == 0) {
            System.err.println("Немає факультетів! Спочатку створіть факультет.");
            return;
        }

        Faculty selectedFaculty = null;
        boolean facultySelected = false;

        while (!facultySelected) {
            System.out.println("\n=== Оберіть факультет ===");
            for (int i = 0; i < faculties.length; i++) {
                System.out.println((i + 1) + ". " + faculties[i].getShortName() + " - " + faculties[i].getName());
            }

            System.out.print("Номер факультету: ");
            String facultyInput = scanner.nextLine().trim();

            if (facultyInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім! Спробуйте ще раз.");
                continue;
            }

            try {
                int facultyChoice = Integer.parseInt(facultyInput);
                if (facultyChoice < 1 || facultyChoice > faculties.length) {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + faculties.length);
                    continue;
                }
                selectedFaculty = faculties[facultyChoice - 1];
                facultySelected = true;
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        int code = departmentRepository.getNextCode();
        System.out.println("\nАвтоматично згенерований код: " + code);

        String name = "";
        do {
            System.out.print("Введіть назву кафедри: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Назва кафедри не може бути порожньою.");
            }
        } while (name.isEmpty());

        String cabinet = "";
        do {
            System.out.print("Введіть номер кабінету: ");
            cabinet = scanner.nextLine().trim();
            if (cabinet.isEmpty()) {
                System.out.println("Номер кабінету не може бути порожнім.");
            }
        } while (cabinet.isEmpty());

        Teacher[] allTeachers = teacherRepository.getTeachers();
        Teacher selectedHead = null;

        if (allTeachers.length == 0) {
            System.out.println("Немає викладачів. Кафедра буде створена без завідувача.");
        } else {
            System.out.print("\nПризначити завідувача кафедри? (y/n): ");
            String assignHead = scanner.nextLine().trim();

            if (assignHead.equalsIgnoreCase("y")) {
                boolean headSelected = false;

                while (!headSelected) {
                    System.out.println("\n=== Оберіть завідувача зі списку викладачів ===");
                    for (int i = 0; i < allTeachers.length; i++) {
                        String facultyName = allTeachers[i].getFaculty() != null ?
                                allTeachers[i].getFaculty().getShortName() : "немає";
                        System.out.println((i + 1) + ". [ID: " + allTeachers[i].getId() + "] " +
                                allTeachers[i].getFullName() + " - " + allTeachers[i].getPosition() +
                                " (" + facultyName + ")");
                    }
                    System.out.println("0. Пропустити (без завідувача)");

                    System.out.print("Номер викладача: ");
                    String headInput = scanner.nextLine().trim();

                    if (headInput.isEmpty()) {
                        System.err.println("Введення не може бути порожнім!");
                        continue;
                    }

                    try {
                        int headChoice = Integer.parseInt(headInput);
                        if (headChoice == 0) {
                            selectedHead = null;
                            headSelected = true;
                        } else if (headChoice >= 1 && headChoice <= allTeachers.length) {
                            selectedHead = allTeachers[headChoice - 1];
                            headSelected = true;
                        } else {
                            System.err.println("Невірний вибір! Оберіть номер від 0 до " + allTeachers.length);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Невірний формат! Введіть число.");
                    }
                }
            }
        }

        Department newDepartment = new Department(code, name, selectedFaculty, selectedHead, cabinet);
        departmentRepository.addDepartment(newDepartment);

        System.out.println("Кафедру успішно додано до факультету " + selectedFaculty.getShortName() + "!");
        if (selectedHead != null) {
            System.out.println("   Завідувач: " + selectedHead.getFullName());
        }
    }

    public void deleteDepartment() {
        System.out.println("---Видалити кафедру---");

        Department[] departments = departmentRepository.getDepartments();
        if (departments.length == 0) {
            System.err.println("Немає кафедр для видалення!");
            return;
        }

        System.out.println("\n=== Список кафедр ===");
        for (int i = 0; i < departments.length; i++) {
            String facultyName = departments[i].getFaculty() != null ?
                    departments[i].getFaculty().getShortName() : "немає";
            System.out.println((i + 1) + ". [Код: " + departments[i].getCode() + "] " +
                    departments[i].getName() + " (Факультет: " + facultyName + ", Каб: " +
                    departments[i].getCabinet() + ")");
        }

        System.out.print("\nВведіть код кафедри для видалення: ");
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

        boolean deleted = departmentRepository.deleteDepartmentByCode(code);

        if (deleted) {
            System.out.println("Кафедру успішно видалено!");
        } else {
            System.err.println("Кафедру не знайдено!");
        }
    }


    public void updateDepartment() {
        System.out.println("---Редагувати кафедру---");

        Department[] departments = departmentRepository.getDepartments();
        if (departments.length == 0) {
            System.err.println("Немає кафедр для редагування!");
            return;
        }

        System.out.println("\n=== Список кафедр ===");
        for (int i = 0; i < departments.length; i++) {
            String facultyName = departments[i].getFaculty() != null ?
                    departments[i].getFaculty().getShortName() : "немає";
            String headName = departments[i].getHead() != null ?
                    departments[i].getHead().getFullName() : "не призначено";
            System.out.println((i + 1) + ". [Код: " + departments[i].getCode() + "] " +
                    departments[i].getName() + " (Факультет: " + facultyName +
                    ", Завідувач: " + headName + ")");
        }

        System.out.print("\nВведіть код кафедри для редагування: ");
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

        Department currentDepartment = null;
        for (Department d : departments) {
            if (d.getCode() == code) {
                currentDepartment = d;
                break;
            }
        }

        if (currentDepartment == null) {
            System.err.println("Кафедру не знайдено!");
            return;
        }

        System.out.println("\n--- Редагування кафедри (введіть 's' щоб залишити поточне значення) ---");

        String newName = "";
        do {
            System.out.print("Назва кафедри [" + currentDepartment.getName() + "]: ");
            newName = scanner.nextLine().trim();

            if (newName.equalsIgnoreCase("s") || newName.isEmpty()) {
                newName = currentDepartment.getName();
                break;
            }

            if (newName.isEmpty()) {
                System.out.println("Назва не може бути порожньою.");
            }
        } while (newName.isEmpty());

        String newCabinet = "";
        do {
            System.out.print("Номер кабінету [" + currentDepartment.getCabinet() + "]: ");
            newCabinet = scanner.nextLine().trim();

            if (newCabinet.equalsIgnoreCase("s") || newCabinet.isEmpty()) {
                newCabinet = currentDepartment.getCabinet();
                break;
            }

            if (newCabinet.isEmpty()) {
                System.out.println("Номер кабінету не може бути порожнім.");
            }
        } while (newCabinet.isEmpty());

        System.out.println("\nПоточний факультет: " +
                (currentDepartment.getFaculty() != null ? currentDepartment.getFaculty().getShortName() : "не вказано"));
        System.out.print("Змінити факультет? (y/n): ");
        String changeFaculty = scanner.nextLine().trim();

        Faculty newFaculty = currentDepartment.getFaculty();

        if (changeFaculty.equalsIgnoreCase("y")) {
            Faculty[] faculties = facultyRepository.getFaculties();
            if (faculties.length == 0) {
                System.err.println("Немає факультетів!");
            } else {
                Faculty selectedFaculty = null;
                boolean facultySelected = false;

                while (!facultySelected) {
                    System.out.println("\n=== Оберіть новий факультет ===");
                    for (int i = 0; i < faculties.length; i++) {
                        System.out.println((i + 1) + ". " + faculties[i].getShortName() + " - " + faculties[i].getName());
                    }
                    System.out.print("Номер факультету: ");
                    String facultyInput = scanner.nextLine().trim();

                    if (facultyInput.isEmpty()) {
                        System.err.println("Введення не може бути порожнім!");
                        continue;
                    }

                    try {
                        int facultyChoice = Integer.parseInt(facultyInput);
                        if (facultyChoice >= 1 && facultyChoice <= faculties.length) {
                            selectedFaculty = faculties[facultyChoice - 1];
                            newFaculty = selectedFaculty;
                            facultySelected = true;
                        } else {
                            System.err.println("Невірний вибір! Оберіть номер від 1 до " + faculties.length);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Невірний формат! Введіть число.");
                    }
                }
            }
        }

        System.out.println("\nПоточний завідувач: " +
                (currentDepartment.getHead() != null ? currentDepartment.getHead().getFullName() : "не призначено"));
        System.out.print("Змінити завідувача? (y/n): ");
        String changeHead = scanner.nextLine().trim();

        Teacher newHead = currentDepartment.getHead();

        if (changeHead.equalsIgnoreCase("y")) {
            Teacher[] allTeachers = teacherRepository.getTeachers();
            if (allTeachers.length == 0) {
                System.err.println("Немає викладачів!");
            } else {
                boolean headSelected = false;

                while (!headSelected) {
                    System.out.println("\n=== Оберіть завідувача зі списку викладачів ===");
                    for (int i = 0; i < allTeachers.length; i++) {
                        String facultyName = allTeachers[i].getFaculty() != null ?
                                allTeachers[i].getFaculty().getShortName() : "немає";
                        System.out.println((i + 1) + ". [ID: " + allTeachers[i].getId() + "] " +
                                allTeachers[i].getFullName() + " - " + allTeachers[i].getPosition() +
                                " (" + facultyName + ")");
                    }
                    System.out.println("0. Видалити завідувача (залишити без завідувача)");

                    System.out.print("Номер викладача: ");
                    String headInput = scanner.nextLine().trim();

                    if (headInput.isEmpty()) {
                        System.err.println("Введення не може бути порожнім!");
                        continue;
                    }

                    try {
                        int headChoice = Integer.parseInt(headInput);
                        if (headChoice == 0) {
                            newHead = null;
                            headSelected = true;
                        } else if (headChoice >= 1 && headChoice <= allTeachers.length) {
                            newHead = allTeachers[headChoice - 1];
                            headSelected = true;
                        } else {
                            System.err.println("Невірний вибір! Оберіть номер від 0 до " + allTeachers.length);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Невірний формат! Введіть число.");
                    }
                }
            }
        }

        Department updatedDepartment = new Department(code, newName, newFaculty, newHead, newCabinet);

        boolean updated = departmentRepository.updateDepartment(code, updatedDepartment);

        if (updated) {
            System.out.println("Кафедру успішно оновлено!");
            if (newHead != null) {
                System.out.println("   Завідувач: " + newHead.getFullName());
            }
        } else {
            System.err.println("Помилка при оновленні!");
        }
    }
}
