package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.repository.*;

import java.util.Optional;
import java.util.Scanner;

public class StudentOperations {
    private final Scanner scanner = new Scanner(System.in);
    private final InputValidator inputValidator = new InputValidator();
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    public StudentOperations(StudentRepository studentRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
    }


    /*
 Displays students list
 */
    public void showStudents(Student[] students) {
        if (students == null || students.length == 0) {
            System.out.println("Інформацію про студентів не знайдено");
            return;
        }
        System.out.println("---Список студентів---");
        for (Student student : students) {
            System.out.println(student.toString());
        }
    }

    /**
     * Adds a new student to the repository
     * Manager writes full name, group format, enrollment year, date of birth, email,
     * phone number, student ID, course, study form, and status.
     */
    public void addStudent() {
        System.out.println("---Додати нового студента---");

        int id = studentRepository.getNextId();
        System.out.println("Автоматично згенерований ID: " + id);

        String firstName = "";
        do {
            System.out.print("Введіть ім'я: ");
            firstName = scanner.nextLine().trim();
            if (inputValidator.checkWord(firstName).equals("-1")) {
                System.out.println("Невірне ім'я. Використовуйте тільки літери.");
            }
        } while (inputValidator.checkWord(firstName).equals("-1"));

        String middleName = "";
        do {
            System.out.print("Введіть по батькові: ");
            middleName = scanner.nextLine().trim();
            if (inputValidator.checkWord(middleName).equals("-1")) {
                System.out.println("Невірне ім'я. Використовуйте тільки літери.");
            }
        } while (inputValidator.checkWord(middleName).equals("-1"));

        String lastName = "";
        do {
            System.out.print("Введіть прізвище: ");
            lastName = scanner.nextLine().trim();
            if (inputValidator.checkWord(lastName).equals("-1")) {
                System.out.println("Невірне ім'я. Використовуйте тільки літери.");
            }
        } while (inputValidator.checkWord(lastName).equals("-1"));

        String birthDate = "";
        do {
            System.out.print("Введіть дату народження (DD.MM.YYYY): ");
            birthDate = scanner.nextLine().trim();
            birthDate = inputValidator.checkFullDate(birthDate, 1920, 2012);
            if (birthDate.equals("-1")) {
                System.out.println("Невірна дата. Формат: DD.MM.YYYY (роки 1920 - 2012)");
            }
        } while (birthDate.equals("-1"));

        String email = "";
        do {
            System.out.print("Введіть email: ");
            email = scanner.nextLine().trim();
            email = inputValidator.checkEmail(email);
            if (email.equals("-1")) {
                System.out.println("Невірний email. Приклад: example@ukma.edu.ua");
            }
        } while (email.equals("-1"));

        String phoneNumber = "";
        do {
            System.out.print("Введіть номер телефону (+380XXXXXXXXX): ");
            phoneNumber = scanner.nextLine().trim();
            phoneNumber = inputValidator.checkPhoneNumber(phoneNumber);
            if (phoneNumber.equals("-1")) {
                System.out.println("Невірний номер телефону. Формат: +380XXXXXXXXX");
            }
        } while (phoneNumber.equals("-1"));

        String studentID = "";
        do {
            System.out.print("Введіть студентський квиток (6 символів, напр., 4BI92H): ");
            studentID = scanner.nextLine().trim();
            studentID = inputValidator.checkStudentID(studentID);
            if (studentID.equals("-1")) {
                System.out.println("Невірний формат студентського квитка. Приклад: '4BI92H'");
            }
        } while (studentID.equals("-1"));

        int course = 0;
        boolean validCourse = false;
        do {
            try {
                System.out.print("Введіть курс (1-6): ");
                course = Integer.parseInt(scanner.nextLine().trim());
                course = inputValidator.checkCourse(course);
                if (course == -1) {
                    System.out.println("Курс повинен бути від 1 до 6.");
                } else {
                    validCourse = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть коректне число.");
            }
        } while (!validCourse);

        String group = "";
        do {
            System.out.print("Введіть групу (напр., 12A): ");
            group = scanner.nextLine().trim();
            group = inputValidator.checkGroup(group);
            if (group.equals("-1")) {
                System.out.println("Невірний формат групи. Приклад: '12A'");
            }
        } while (group.equals("-1"));

        int yearEnroll = 0;
        boolean validYear = false;
        do {
            try {
                System.out.print("Введіть рік вступу: ");
                yearEnroll = Integer.parseInt(scanner.nextLine().trim());
                yearEnroll = inputValidator.checkYearEnroll(yearEnroll);

                if (yearEnroll == -1) {
                    System.out.println("Рік повинен бути від 1992 до 2026.");
                } else {
                    validYear = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть коректне число.");
            }
        } while (!validYear);

        String studyForm = "";
        do {
            System.out.print("Введіть форму навчання (бюджет/контракт): ");
            studyForm = scanner.nextLine().trim();
            studyForm = inputValidator.checkStudyForm(studyForm);
            if (studyForm.equals("-1")) {
                System.out.println("Невірна форма навчання. Використовуйте: бюджет/контракт");
            }
        } while (studyForm.equals("-1"));

        String status = "";
        do {
            System.out.print("Введіть статус (навчається/академвідпустка/відрахований) : ");
            status = scanner.nextLine().trim();
            status = inputValidator.checkStatus(status);
            if (status.equals("-1")) {
                System.out.println("Невірний статус. Використовуйте: навчається/академвідпустка/відрахований");
            }
        } while (status.equals("-1"));

        Faculty[] faculties = facultyRepository.getFaculties();
        if (faculties.length == 0) {
            System.err.println("Немає факультетів!");
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

        Department[] departments = departmentRepository.findByFaculty(selectedFaculty);
        if (departments.length == 0) {
            System.err.println("На цьому факультеті немає кафедр!");
            return;
        }

        Department selectedDepartment = null;
        boolean departmentSelected = false;

        while (!departmentSelected) {
            System.out.println("\n=== Оберіть кафедру ===");
            for (int i = 0; i < departments.length; i++) {
                System.out.println((i + 1) + ". " + departments[i].getName());
            }

            System.out.print("Номер кафедри: ");
            String deptInput = scanner.nextLine().trim();

            if (deptInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім! Спробуйте ще раз.");
                continue;
            }

            try {
                int deptChoice = Integer.parseInt(deptInput);
                if (deptChoice < 1 || deptChoice > departments.length) {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + departments.length);
                    continue;
                }
                selectedDepartment = departments[deptChoice - 1];
                departmentSelected = true;
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        Student newStudent = new Student(id, firstName, middleName, lastName, birthDate, email,
                phoneNumber, studentID, course, group, yearEnroll, studyForm, status,
                selectedDepartment);

        studentRepository.addStudent(newStudent);

        System.out.println("\nСтудента успішно додано!");
        System.out.println("\n---Інформація про створеного студента---");
        System.out.println(newStudent.toString());
    }


    /**
     * Deletes a student from the repository by ID or full name.
     * Provides two search options and displays success or error message based on result.
     */
    public void deleteStudent() {
        System.out.println("---Видалити студента---");

        Student[] allStudents = studentRepository.getStudents();
        showStudents(allStudents);

        System.out.println("\nОберіть метод видалення:");
        System.out.println("1. По ID (число)");
        System.out.println("2. По повному імені");
        System.out.print("Введіть вибір (1 або 2): ");

        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Невірне введення!");
            return;
        }

        boolean deleted = false;

        switch (choice) {
            case 1:
                // Deleting by ID
                System.out.print("Введіть ID студента (число): ");
                try {
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    deleted = this.studentRepository.deleteStudentById(id);
                } catch (NumberFormatException e) {
                    System.err.println("Невірний формат ID!");
                    return;
                }
                break;

            case 2:
                // Deleting by full name
                String[] names = {"first", "middle", "last"};
                String firstName = "";
                String middleName = "";
                String lastName = "";

                do {
                    System.out.print("Введіть ім'я: ");
                    firstName = scanner.nextLine().trim();
                    if (inputValidator.checkWord(firstName).equals("-1")) {
                        System.out.println("Невірне ім'я. Використовуйте тільки літери.");
                    }
                } while (inputValidator.checkWord(firstName).equals("-1"));

                do {
                    System.out.print("Введіть по батькові: ");
                    middleName = scanner.nextLine().trim();
                    if (inputValidator.checkWord(middleName).equals("-1")) {
                        System.out.println("Невірне ім'я. Використовуйте тільки літери.");
                    }
                } while (inputValidator.checkWord(middleName).equals("-1"));

                do {
                    System.out.print("Введіть прізвище: ");
                    lastName = scanner.nextLine().trim();
                    if (inputValidator.checkWord(lastName).equals("-1")) {
                        System.out.println("Невірне ім'я. Використовуйте тільки літери.");
                    }
                } while (inputValidator.checkWord(lastName).equals("-1"));

                String[] fullName = {firstName, middleName, lastName};
                deleted = this.studentRepository.deleteStudentByFullName(fullName);
                break;

            default:
                System.err.println("Невірний вибір!");
                return;
        }

        if (deleted) {
            System.out.println("Студента успішно видалено!");
        } else {
            System.err.println("Студента не знайдено!");
        }
    }

    /**
     * Updates an existing student's information.
     * All the information about student (full name, group format, enrollment year, date of birth, email,
     * phone number, student ID, course, study form, and status) can be changed.
     * Write 's' (same) to keep current value.
     */
    public void updateStudent() {
        System.out.println("---Редагувати студента---");

        Student[] allStudents = studentRepository.getStudents();
        showStudents(allStudents);

        System.out.println("\nОберіть метод пошуку:");
        System.out.println("1. По ID (число)");
        System.out.println("2. По повному імені");
        System.out.print("Введіть вибір (1 або 2): ");

        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Невірне введення!");
            return;
        }

        Student currentStudent = null;

        switch (choice) {
            case 1:
                System.out.print("Введіть ID студента (число): ");
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
                String firstName = "";
                String middleName = "";
                String lastName = "";

                do {
                    System.out.print("Введіть ім'я: ");
                    firstName = scanner.nextLine().trim();
                    if (inputValidator.checkWord(firstName).equals("-1")) {
                        System.out.println("Невірне ім'я. Використовуйте тільки літери.");
                    }
                } while (inputValidator.checkWord(firstName).equals("-1"));

                do {
                    System.out.print("Введіть по батькові: ");
                    middleName = scanner.nextLine().trim();
                    if (inputValidator.checkWord(middleName).equals("-1")) {
                        System.out.println("Невірне ім'я. Використовуйте тільки літери.");
                    }
                } while (inputValidator.checkWord(middleName).equals("-1"));

                do {
                    System.out.print("Введіть прізвище: ");
                    lastName = scanner.nextLine().trim();
                    if (inputValidator.checkWord(lastName).equals("-1")) {
                        System.out.println("Невірне ім'я. Використовуйте тільки літери.");
                    }
                } while (inputValidator.checkWord(lastName).equals("-1"));

                String[] fullName = {firstName, middleName, lastName};
                Optional<Student> studentOpt = studentRepository.FindByFullName(fullName);
                if (studentOpt.isPresent()) {
                    currentStudent = studentOpt.get();
                }
                break;

            default:
                System.err.println("Невірний вибір!");
                return;
        }

        if (currentStudent == null) {
            System.err.println("\nСтудента не знайдено!");
            return;
        }

        System.out.println("\n--- Редагування інформації про студента (введіть 's' щоб залишити поточне значення) ---");

        int id = currentStudent.getId();

        String newFirstName = "";
        do {
            System.out.print("Введіть ім'я [" + currentStudent.getFirstName() + "]: ");
            newFirstName = scanner.nextLine().trim();

            if (newFirstName.equalsIgnoreCase("s") || newFirstName.isEmpty()) {
                newFirstName = currentStudent.getFirstName();
                break;
            }

            if (inputValidator.checkWord(newFirstName).equals("-1")) {
                System.out.println("Невірне ім'я. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newFirstName).equals("-1"));

        String newMiddleName = "";
        do {
            System.out.print("Введіть по батькові [" + currentStudent.getMiddleName() + "]: ");
            newMiddleName = scanner.nextLine().trim();

            if (newMiddleName.equalsIgnoreCase("s") || newMiddleName.isEmpty()) {
                newMiddleName = currentStudent.getMiddleName();
                break;
            }

            if (inputValidator.checkWord(newMiddleName).equals("-1")) {
                System.out.println("Невірне ім'я. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newMiddleName).equals("-1"));

        String newLastName = "";
        do {
            System.out.print("Введіть прізвище [" + currentStudent.getLastName() + "]: ");
            newLastName = scanner.nextLine().trim();

            if (newLastName.equalsIgnoreCase("s") || newLastName.isEmpty()) {
                newLastName = currentStudent.getLastName();
                break;
            }

            if (inputValidator.checkWord(newLastName).equals("-1")) {
                System.out.println("Невірне ім'я. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newLastName).equals("-1"));

        String newBirthDate = "";
        do {
            System.out.print("Введіть дату народження [" + currentStudent.getBirthDate() + "] (DD.MM.YYYY): ");
            newBirthDate = scanner.nextLine().trim();

            if (newBirthDate.equalsIgnoreCase("s") || newBirthDate.isEmpty()) {
                newBirthDate = currentStudent.getBirthDate();
                break;
            }

            newBirthDate = inputValidator.checkFullDate(newBirthDate, 1920, 2012);
            if (newBirthDate.equals("-1")) {
                System.out.println("Невірна дата. Формат: DD.MM.YYYY (роки 1920 - 2912) або 's'.");
            }
        } while (newBirthDate.equals("-1"));

        String newEmail = "";
        do {
            System.out.print("Введіть email [" + currentStudent.getEmail() + "]: ");
            newEmail = scanner.nextLine().trim();

            if (newEmail.equalsIgnoreCase("s") || newEmail.isEmpty()) {
                newEmail = currentStudent.getEmail();
                break;
            }

            newEmail = inputValidator.checkEmail(newEmail);
            if (newEmail.equals("-1")) {
                System.out.println("Невірний email. Приклад: example@ukma.edu.ua або 's'.");
            }
        } while (newEmail.equals("-1"));

        String newPhoneNumber = "";
        do {
            System.out.print("Введіть номер телефону [" + currentStudent.getPhoneNumber() + "] (+380XXXXXXXXX): ");
            newPhoneNumber = scanner.nextLine().trim();

            if (newPhoneNumber.equalsIgnoreCase("s") || newPhoneNumber.isEmpty()) {
                newPhoneNumber = currentStudent.getPhoneNumber();
                break;
            }

            newPhoneNumber = inputValidator.checkPhoneNumber(newPhoneNumber);
            if (newPhoneNumber.equals("-1")) {
                System.out.println("Невірний номер телефону. Формат: +380XXXXXXXXX або 's'.");
            }
        } while (newPhoneNumber.equals("-1"));

        String newStudentID = "";
        do {
            System.out.print("Введіть студентський кв��ток [" + currentStudent.getStudentId() + "] (6 символів, напр., 4BI92H): ");
            newStudentID = scanner.nextLine().trim();

            if (newStudentID.equalsIgnoreCase("s") || newStudentID.isEmpty()) {
                newStudentID = currentStudent.getStudentId();
                break;
            }

            newStudentID = inputValidator.checkStudentID(newStudentID);
            if (newStudentID.equals("-1")) {
                System.out.println("Невірний формат студентського квитка. Приклад: '4BI92H' або 's'.");
            }
        } while (newStudentID.equals("-1"));

        int newCourse = 0;
        boolean validCourse = false;
        do {
            try {
                System.out.print("Введіть курс [" + currentStudent.getCourse() + "] (1-6): ");
                String courseInput = scanner.nextLine().trim();

                if (courseInput.equalsIgnoreCase("s") || courseInput.isEmpty()) {
                    newCourse = currentStudent.getCourse();
                    validCourse = true;
                } else {
                    newCourse = Integer.parseInt(courseInput);
                    newCourse = inputValidator.checkCourse(newCourse);
                    if (newCourse == -1) {
                        System.out.println("Курс повинен бути від 1 до 6 або 's'.");
                    } else {
                        validCourse = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть коректне число або 's'.");
            }
        } while (!validCourse);

        String newGroup = "";
        do {
            System.out.print("Введіть групу [" + currentStudent.getGroup() + "] (напр., 12A): ");
            newGroup = scanner.nextLine().trim();

            if (newGroup.equalsIgnoreCase("s") || newGroup.isEmpty()) {
                newGroup = currentStudent.getGroup();
                break;
            }

            newGroup = inputValidator.checkGroup(newGroup);
            if (newGroup.equals("-1")) {
                System.out.println("Невірний формат групи. Приклад: '12A' або 's'.");
            }
        } while (newGroup.equals("-1"));

        int newYearEnroll = 0;
        boolean validYear = false;
        do {
            try {
                System.out.print("Введіть рік вступу [" + currentStudent.getYearEnroll() + "]: ");
                String yearInput = scanner.nextLine().trim();

                if (yearInput.equalsIgnoreCase("s") || yearInput.isEmpty()) {
                    newYearEnroll = currentStudent.getYearEnroll();
                    validYear = true;
                } else {
                    newYearEnroll = Integer.parseInt(yearInput);
                    newYearEnroll = inputValidator.checkYearEnroll(newYearEnroll);
                    if (newYearEnroll == -1) {
                        System.out.println("Рік повинен бути від 1992 до 2026 або 's'.");
                    } else {
                        validYear = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть коректний рік або 's'.");
            }
        } while (!validYear);

        String newStudyForm = "";
        do {
            System.out.print("Введіть форму навчання [" + currentStudent.getStudyForm() + "] (бюджет/контракт): ");
            newStudyForm = scanner.nextLine().trim();

            if (newStudyForm.equalsIgnoreCase("s") || newStudyForm.isEmpty()) {
                newStudyForm = currentStudent.getStudyForm();
                break;
            }

            newStudyForm = inputValidator.checkStudyForm(newStudyForm);
            if (newStudyForm.equals("-1")) {
                System.out.println("Невірна форма навчання. Використовуйте: бюджет/контракт або 's'.");
            }
        } while (newStudyForm.equals("-1"));

        String newStatus = "";
        do {
            System.out.print("Введіть статус [" + currentStudent.getStatus() + "] (навчається/академвідпустка/відрахований): ");
            newStatus = scanner.nextLine().trim();

            if (newStatus.equalsIgnoreCase("s") || newStatus.isEmpty()) {
                newStatus = currentStudent.getStatus();
                break;
            }

            newStatus = inputValidator.checkStatus(newStatus);
            if (newStatus.equals("-1")) {
                System.out.println("Невірний статус. Використовуйте: навчається/академвідпустка/відрахований або 's'.");
            }
        } while (newStatus.equals("-1"));

        System.out.println("\nПоточний факультет: " +
                (currentStudent.getFaculty() != null ? currentStudent.getFaculty().getShortName() : "не вказано"));
        System.out.println("Поточна кафедра: " +
                (currentStudent.getDepartment() != null ? currentStudent.getDepartment().getName() : "не вказано"));

        System.out.print("\nЗмінити кафедру? (y/n): ");
        String changeDept = scanner.nextLine().trim();

        Department selectedDepartment = currentStudent.getDepartment();

        if (changeDept.equalsIgnoreCase("y")) {
            Faculty[] faculties = facultyRepository.getFaculties();
            if (faculties.length == 0) {
                System.err.println("Немає факультетів!");
            } else {
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
                        System.err.println("Введення не може бути порожнім!");
                        continue;
                    }

                    try {
                        int facultyChoice = Integer.parseInt(facultyInput);
                        if (facultyChoice >= 1 && facultyChoice <= faculties.length) {
                            selectedFaculty = faculties[facultyChoice - 1];
                            facultySelected = true;
                        } else {
                            System.err.println("Невірний вибір! Оберіть номер від 1 до " + faculties.length);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Невірний формат! Введіть число.");
                    }
                }

                Department[] departments = departmentRepository.findByFaculty(selectedFaculty);
                if (departments.length == 0) {
                    System.err.println("На цьому факультеті немає кафедр!");
                } else {
                    boolean departmentSelected = false;

                    while (!departmentSelected) {
                        System.out.println("\n=== Оберіть кафедру ===");
                        for (int i = 0; i < departments.length; i++) {
                            System.out.println((i + 1) + ". " + departments[i].getName());
                        }
                        System.out.print("Номер кафедри: ");
                        String deptInput = scanner.nextLine().trim();

                        if (deptInput.isEmpty()) {
                            System.err.println("Введення не може бути порожнім!");
                            continue;
                        }

                        try {
                            int deptChoice = Integer.parseInt(deptInput);
                            if (deptChoice >= 1 && deptChoice <= departments.length) {
                                selectedDepartment = departments[deptChoice - 1];
                                departmentSelected = true;
                            } else {
                                System.err.println("Невірний вибір! Оберіть номер від 1 до " + departments.length);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Невірний формат! Введіть число.");
                        }
                    }
                }
            }
        }

        Student updatedStudent = new Student(id, newFirstName, newMiddleName, newLastName, newBirthDate, newEmail,
                newPhoneNumber, newStudentID, newCourse, newGroup, newYearEnroll, newStudyForm, newStatus,
                selectedDepartment);

        boolean updated = studentRepository.updateStudent(id, updatedStudent);

        if (updated) {
            System.out.println("\nСтудента успішно оновлено!");
        } else {
            System.err.println("\nНе вдалося оновити студента!");
        }
    }
}
