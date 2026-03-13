package ua.university.service;

import ua.university.domain.*;
import ua.university.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TransferOperations {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final InputValidator inputValidator;
    private final Scanner scanner = new Scanner(System.in);

    public TransferOperations(StudentRepository studentRepository,
                              UniversityRepository universityRepository,
                              FacultyRepository facultyRepository,
                              DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.inputValidator = new InputValidator();
    }

    /**
     * Transfers student to other department
     */
    public void transferStudentToDepartment() {
        System.out.println("---Переведення студента на іншу кафедру---");

        List<Student> allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) {
            System.err.println("Немає студентів для переведення!");
            return;
        }

        System.out.println("\n=== Список студентів ===");
        for (int i = 0; i < allStudents.size(); i++) {
            String facultyName = allStudents.get(i).getFaculty() != null ?
                    allStudents.get(i).getFaculty().getShortName() : "немає";
            String deptName = allStudents.get(i).getDepartment() != null ?
                    allStudents.get(i).getDepartment().getName() : "немає";
            System.out.println((i + 1) + ". [ID: " + allStudents.get(i).getId() + "] " +
                    allStudents.get(i).getFullName() + " - Курс: " + allStudents.get(i).getCourse() +
                    ", Група: " + allStudents.get(i).getGroup() +
                    " (" + facultyName + ", " + deptName + ")");
        }

        System.out.println("\nОберіть метод пошуку студента:");
        System.out.println("1. По ID");
        System.out.println("2. По повному імені");
        System.out.println("3. По student ID");
        System.out.print("Ваш вибір (1-3): ");

        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Невірний формат!");
            return;
        }

        Student currentStudent = null;

        switch (choice) {
            case 1:
                System.out.print("Введіть ID студента: ");
                try {
                    int searchId = Integer.parseInt(scanner.nextLine().trim());
                    Optional<Student> studentOpt = studentRepository.findById(searchId);
                    if (studentOpt.isPresent()) {
                        currentStudent = studentOpt.get();
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Невірний формат ID!");
                    return;
                }
                break;

            case 2:
                System.out.print("Введіть ім'я: ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Введіть по батькові: ");
                String middleName = scanner.nextLine().trim();
                System.out.print("Введіть прізвище: ");
                String lastName = scanner.nextLine().trim();

                String[] fullName = {firstName, middleName, lastName};
                Optional<Student> studentOptByName = studentRepository.FindByFullName(fullName);
                if (studentOptByName.isPresent()) {
                    currentStudent = studentOptByName.get();
                }
                break;

            case 3:
                System.out.print("Введіть student ID: ");
                String studentId = scanner.nextLine().trim();
                Optional<Student> studentOptById = studentRepository.FindByStudentID(studentId);
                if (studentOptById.isPresent()) {
                    currentStudent = studentOptById.get();
                }
                break;

            default:
                System.err.println("Невірний вибір!");
                return;
        }

        if (currentStudent == null) {
            System.err.println("Студента не знайдено!");
            return;
        }

        System.out.println("\n=== Поточна інформація про студента ===");
        System.out.println("ПІБ: " + currentStudent.getFullName());
        System.out.println("Поточний факультет: " +
                (currentStudent.getFaculty() != null ? currentStudent.getFaculty().getShortName() : "не вказано"));
        System.out.println("Поточна кафедра: " +
                (currentStudent.getDepartment() != null ? currentStudent.getDepartment().getName() : "не вказано"));
        System.out.println("Курс: " + currentStudent.getCourse());
        System.out.println("Група: " + currentStudent.getGroup());

        List<Faculty> faculties = facultyRepository.findAll();
        if (faculties.isEmpty()) {
            System.err.println("Немає факультетів!");
            return;
        }

        Faculty selectedFaculty = null;
        boolean facultySelected = false;

        while (!facultySelected) {
            System.out.println("\n=== Оберіть новий факультет ===");
            for (int i = 0; i < faculties.size(); i++) {
                System.out.println((i + 1) + ". " + faculties.get(i).getShortName() + " - " + faculties.get(i).getName());
            }

            System.out.print("Номер факультету: ");
            String facultyInput = scanner.nextLine().trim();

            if (facultyInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім!");
                continue;
            }

            try {
                int facultyChoice = Integer.parseInt(facultyInput);
                if (facultyChoice >= 1 && facultyChoice <= faculties.size()) {
                    selectedFaculty = faculties.get(facultyChoice - 1);
                    facultySelected = true;
                } else {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + faculties.size());
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        List<Department> departments = departmentRepository.findByFaculty(selectedFaculty);
        if (departments.isEmpty()) {
            System.err.println("На цьому факультеті немає кафедр!");
            return;
        }

        Department selectedDepartment = null;
        boolean departmentSelected = false;

        while (!departmentSelected) {
            System.out.println("\n=== Оберіть нову кафедру ===");
            for (int i = 0; i < departments.size(); i++) {
                System.out.println((i + 1) + ". " + departments.get(i).getName());
            }

            System.out.print("Номер кафедри: ");
            String deptInput = scanner.nextLine().trim();

            if (deptInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім!");
                continue;
            }

            try {
                int deptChoice = Integer.parseInt(deptInput);
                if (deptChoice >= 1 && deptChoice <= departments.size()) {
                    selectedDepartment = departments.get(deptChoice - 1);
                    departmentSelected = true;
                } else {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + departments.size());
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        Student updatedStudent = new Student(
                currentStudent.getId(),
                currentStudent.getFirstName(),
                currentStudent.getMiddleName(),
                currentStudent.getLastName(),
                currentStudent.getBirthDate(),
                currentStudent.getEmail(),
                currentStudent.getPhoneNumber(),
                currentStudent.getStudentId(),
                currentStudent.getCourse(),
                currentStudent.getGroup(),
                currentStudent.getYearEnroll(),
                currentStudent.getStudyForm(),
                currentStudent.getStatus(),
                selectedDepartment
        );

        boolean updated = studentRepository.update(currentStudent.getId(), updatedStudent);

        if (updated) {
            System.out.println("\n Студента " + currentStudent.getFullName() + " успішно переведено!");
            System.out.println("   На кафедру: " + selectedDepartment.getName());
            System.out.println("   Факультет: " + selectedFaculty.getShortName());
        } else {
            System.err.println("\nПомилка при переведенні!");
        }
    }

    /**
     * Transfers student to other course
     */
    public void transferStudentToCourse() {
        System.out.println("---Переведення студента на інший курс---");

        List<Student> allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) {
            System.err.println("Немає студентів для переведення!");
            return;
        }

        System.out.println("\n=== Список студентів ===");
        for (int i = 0; i < allStudents.size(); i++) {
            String facultyName = allStudents.get(i).getFaculty() != null ?
                    allStudents.get(i).getFaculty().getShortName() : "немає";
            String deptName = allStudents.get(i).getDepartment() != null ?
                    allStudents.get(i).getDepartment().getName() : "немає";
            System.out.println((i + 1) + ". [ID: " + allStudents.get(i).getId() + "] " +
                    allStudents.get(i).getFullName() + " - Курс: " + allStudents.get(i).getCourse() +
                    ", Група: " + allStudents.get(i).getGroup() +
                    " (" + facultyName + ", " + deptName + ")");
        }

        System.out.println("\nОберіть метод пошуку студента:");
        System.out.println("1. По ID");
        System.out.println("2. По повному імені");
        System.out.println("3. По student ID");
        System.out.print("Ваш вибір (1-3): ");

        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Невірний формат!");
            return;
        }

        Student currentStudent = null;

        switch (choice) {
            case 1:
                System.out.print("Введіть ID студента: ");
                try {
                    int searchId = Integer.parseInt(scanner.nextLine().trim());
                    Optional<Student> studentOpt = studentRepository.findById(searchId);
                    if (studentOpt.isPresent()) {
                        currentStudent = studentOpt.get();
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Невірний формат ID!");
                    return;
                }
                break;

            case 2:
                System.out.print("Введіть ім'я: ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Введіть по батькові: ");
                String middleName = scanner.nextLine().trim();
                System.out.print("Введіть прізвище: ");
                String lastName = scanner.nextLine().trim();

                String[] fullName = {firstName, middleName, lastName};
                Optional<Student> studentOptByName = studentRepository.FindByFullName(fullName);
                if (studentOptByName.isPresent()) {
                    currentStudent = studentOptByName.get();
                }
                break;

            case 3:
                System.out.print("Введіть student ID: ");
                String studentId = scanner.nextLine().trim();
                Optional<Student> studentOptById = studentRepository.FindByStudentID(studentId);
                if (studentOptById.isPresent()) {
                    currentStudent = studentOptById.get();
                }
                break;

            default:
                System.err.println("Невірний вибір!");
                return;
        }

        if (currentStudent == null) {
            System.err.println("Студента не знайдено!");
            return;
        }

        System.out.println("\n=== Поточна інформація про студента ===");
        System.out.println("ПІБ: " + currentStudent.getFullName());
        System.out.println("Поточний курс: " + currentStudent.getCourse());
        System.out.println("Поточна група: " + currentStudent.getGroup());
        System.out.println("Факультет: " +
                (currentStudent.getFaculty() != null ? currentStudent.getFaculty().getShortName() : "не вказано"));
        System.out.println("Кафедра: " +
                (currentStudent.getDepartment() != null ? currentStudent.getDepartment().getName() : "не вказано"));

        int newCourse = 0;
        do {
            try {
                System.out.print("\nВведіть новий курс (1-6): ");
                String courseInput = scanner.nextLine().trim();
                if (courseInput.isEmpty()) {
                    System.err.println("Курс не може бути порожнім!");
                    continue;
                }
                newCourse = Integer.parseInt(courseInput);
                newCourse = inputValidator.checkCourse(newCourse);
                if (newCourse == -1) {
                    System.err.println("Курс має бути від 1 до 6!");
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
                newCourse = -1;
            }
        } while (newCourse == -1);

        String newGroup = "";
        do {
            System.out.print("Введіть нову групу (формат: 12А): ");
            newGroup = scanner.nextLine().trim();
            newGroup = inputValidator.checkGroup(newGroup);
            if (newGroup.equals("-1")) {
                System.err.println("Невірний формат групи! Формат: 12А (2 цифри + 1 літера)");
            }
        } while (newGroup.equals("-1"));

        Student updatedStudent = new Student(
                currentStudent.getId(),
                currentStudent.getFirstName(),
                currentStudent.getMiddleName(),
                currentStudent.getLastName(),
                currentStudent.getBirthDate(),
                currentStudent.getEmail(),
                currentStudent.getPhoneNumber(),
                currentStudent.getStudentId(),
                newCourse,
                newGroup,
                currentStudent.getYearEnroll(),
                currentStudent.getStudyForm(),
                currentStudent.getStatus(),
                currentStudent.getDepartment()
        );

        boolean updated = studentRepository.update(currentStudent.getId(), updatedStudent);

        if (updated) {
            System.out.println("\n Студента " + currentStudent.getFullName() + " успішно переведено!");
            System.out.println("   На курс: " + newCourse);
            System.out.println("   У групу: " + newGroup);
        } else {
            System.err.println("\nПомилка при переведенні!");
        }
    }
}