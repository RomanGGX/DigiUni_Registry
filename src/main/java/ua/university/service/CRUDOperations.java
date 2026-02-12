package ua.university.service;

import java.util.Optional;
import ua.university.domain.*;
import ua.university.repository.*;

import java.util.Scanner;

public class CRUDOperations {

    private final Scanner scanner = new Scanner(System.in);
    private final InputValidator inputValidator = new InputValidator();
    private final StudentRepository studentRepository;
    private final UniversityRepository universityRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;

    public CRUDOperations(StudentRepository studentRepository, UniversityRepository universityRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.universityRepository = universityRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
    }

    /// ////////////////////
    /// SHOW OPERATIONS ///
    /// ////////////////////
    /*
    Displays universities list
     */
    public void showUniversity() {
        University university = universityRepository.getUniversity();
        System.out.println("---Інформація про університет---");
        System.out.println(university.toString());
    }

    /*
    Displays faculties list
     */
    public void showFaculties(Faculty[] faculties) {
        if (faculties == null || faculties.length == 0) {
            System.out.println("Інформацію про факультети не знайдено");
            return;
        }
        System.out.println("---Список факультетів---");
        for (Faculty faculty : faculties) {
            System.out.println(faculty.toString());
        }
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

    /*
     Displays persons list
     */
    public void showPersons(Person[] persons) {
        if (persons == null || persons.length == 0) {
            System.out.println("Інформацію про осіб не знайдено");
            return;
        }
        System.out.println("---Список осіб---");
        for (Person person : persons) {
            System.out.println(person.toString());
        }
    }

    /*
     Displays teachers list
     */
    public void showTeachers(Teacher[] teachers) {
        if (teachers == null || teachers.length == 0) {
            System.out.println("Інформацію про викладачів не знайдено");
            return;
        }
        System.out.println("---Список викладачів---");
        for (Teacher teacher : teachers) {
            System.out.println(teacher.toString());
        }
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

    ///////////////////////
    /// ADD OPERATIONS ///
    ///////////////////////

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

        Teacher[] allTeachers = teacherRepository.getTeachers();
        Teacher selectedDean = null;

        if (allTeachers.length == 0) {
            System.out.println("Немає викладачів. Факультет буде створено без декана.");
        } else {
            System.out.print("\nПризначити декана? (y/n): ");
            String assignDean = scanner.nextLine().trim();

            if (assignDean.equalsIgnoreCase("y")) {
                boolean deanSelected = false;

                while (!deanSelected) {
                    System.out.println("\n=== Оберіть декана зі списку викладачів ===");
                    for (int i = 0; i < allTeachers.length; i++) {
                        System.out.println((i + 1) + ". [ID: " + allTeachers[i].getId() + "] " +
                                allTeachers[i].getFullName() + " - " + allTeachers[i].getPosition());
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
                        } else if (deanChoice >= 1 && deanChoice <= allTeachers.length) {
                            selectedDean = allTeachers[deanChoice - 1];
                            deanSelected = true;
                        } else {
                            System.err.println("Невірний вибір! Оберіть номер від 0 до " + allTeachers.length);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Невірний формат! Введіть число.");
                    }
                }
            }
        }

        Faculty newFaculty = new Faculty(code, name, shortName, selectedDean, contacts);
        facultyRepository.addFaculty(newFaculty);

        System.out.println("Факультет успішно додано до НаУКМА!");
        if (selectedDean != null) {
            System.out.println("   Декан: " + selectedDean.getFullName());
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

    /**
     * Adds a new teacher to the repository
     */
    public void addTeacher() {
        System.out.println("---Додати нового викладача---");

        int id = teacherRepository.getNextId();
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
                System.out.println("Невірне по батькові. Використовуйте тільки літери.");
            }
        } while (inputValidator.checkWord(middleName).equals("-1"));

        String lastName = "";
        do {
            System.out.print("Введіть прізвище: ");
            lastName = scanner.nextLine().trim();
            if (inputValidator.checkWord(lastName).equals("-1")) {
                System.out.println("Невірне прізвище. Використовуйте тільки літери.");
            }
        } while (inputValidator.checkWord(lastName).equals("-1"));

        String birthDate = "";
        do {
            System.out.print("Введіть дату народження (DD.MM.YYYY): ");
            birthDate = scanner.nextLine().trim();
            birthDate = inputValidator.checkFullDate(birthDate, 1920, 2007);
            if (birthDate.equals("-1")) {
                System.out.println("Невірна дата. Формат: DD.MM.YYYY (рік від 1920 до 2007).");
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
            System.out.print("Введіть телефон (+380XXXXXXXXX): ");
            phoneNumber = scanner.nextLine().trim();
            phoneNumber = inputValidator.checkPhoneNumber(phoneNumber);
            if (phoneNumber.equals("-1")) {
                System.out.println("Невірний номер телефону. Формат: +380XXXXXXXXX");
            }
        } while (phoneNumber.equals("-1"));

        String position = "";
        do {
            System.out.print("Введіть посаду (професор/доцент/асистент/викладач): ");
            position = scanner.nextLine().trim();
            if (position.isEmpty()) {
                System.out.println("Посада не може бути порожньою.");
            }
        } while (position.isEmpty());

        String academicDegree = "";
        System.out.print("Введіть науковий ступінь (доктор наук/доктор філософії або натисніть Enter щоб пропустити): ");
        String degreeInput = scanner.nextLine().trim();

        if (!degreeInput.isEmpty()) {
            do {
                academicDegree = inputValidator.checkAcademicDegree(degreeInput);
                if (academicDegree.equals("-1")) {
                    System.out.println("Невірний науковий ступінь. Оберіть: 'доктор наук' або 'доктор філософії'");
                    System.out.print("Введіть науковий ступінь (або натисніть Enter щоб пропустити): ");
                    degreeInput = scanner.nextLine().trim();
                    if (degreeInput.isEmpty()) {
                        academicDegree = "";
                        break;
                    }
                }
            } while (academicDegree.equals("-1"));
        }

        String academicTitle = "";
        System.out.print("Введіть вчене звання (професор/доцент/старший дослідник або натисніть Enter щоб пропустити): ");
        String titleInput = scanner.nextLine().trim();

        if (!titleInput.isEmpty()) {
            do {
                academicTitle = inputValidator.checkAcademicTitle(titleInput);
                if (academicTitle.equals("-1")) {
                    System.out.println("Невірне вчене звання. Оберіть: 'професор', 'доцент' або 'старший дослідник'");
                    System.out.print("Введіть вчене звання (або натисніть Enter щоб пропустити): ");
                    titleInput = scanner.nextLine().trim();
                    if (titleInput.isEmpty()) {
                        academicTitle = "";
                        break;
                    }
                }
            } while (academicTitle.equals("-1"));
        }

        String employmentDate = "";
        do {
            System.out.print("Введіть дату прийняття на роботу (DD.MM.YYYY): ");
            employmentDate = scanner.nextLine().trim();
            employmentDate = inputValidator.checkFullDate(employmentDate, 1992);
            if (employmentDate.equals("-1")) {
                System.out.println("Невірна дата. Формат: DD.MM.YYYY (рік від 1992 до 2026).");
            }
        } while (employmentDate.equals("-1"));

        double workload = 0.0;
        do {
            try {
                System.out.print("Введіть навантаження (годин): ");
                String workloadInput = scanner.nextLine().trim();
                if (workloadInput.isEmpty()) {
                    System.out.println("Навантаження не може бути порожнім.");
                    continue;
                }
                workload = Double.parseDouble(workloadInput);
                if (workload < 0) {
                    System.out.println("Навантаження не може бути від'ємним.");
                } else if (workload > 1000) {
                    System.out.println("Навантаження занадто велике (макс. 1000 годин).");
                    workload = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
                workload = -1;
            }
        } while (workload < 0);

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

        Teacher newTeacher = new Teacher(id, firstName, middleName, lastName, birthDate, email,
                phoneNumber, position, selectedDepartment, academicDegree, academicTitle,
                employmentDate, workload);

        teacherRepository.addTeacher(newTeacher);

        System.out.println("\nВикладача успішно додано!");
        System.out.println("\n---Інформація про створеного викладача---");
        System.out.println(newTeacher.toString());
    }


    /////////////////////////
    /// DELETE OPERATIONS ///
    /////////////////////////

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

    public void deleteFaculty() {
        System.out.println("---Видалити факультет---");

        Faculty[] faculties = facultyRepository.getFaculties();
        if (faculties.length == 0) {
            System.err.println("Немає факультетів для видалення!");
            return;
        }

        System.out.println("\n=== Список факультетів ===");
        for (int i = 0; i < faculties.length; i++) {
            System.out.println((i + 1) + ". [Код: " + faculties[i].getCode() + "] " +
                    faculties[i].getShortName() + " - " + faculties[i].getName());
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

        boolean deleted = facultyRepository.deleteFacultyByCode(code);

        if (deleted) {
            System.out.println("Факультет успішно видалено!");
        } else {
            System.err.println("Факультет не знайдено!");
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

    public void deleteTeacher() {
        System.out.println("---Видалити викладача---");

        Teacher[] allTeachers = teacherRepository.getTeachers();
        if (allTeachers.length == 0) {
            System.err.println("Немає викладачів для видалення!");
            return;
        }

        System.out.println("\n=== Список викладачів ===");
        for (int i = 0; i < allTeachers.length; i++) {
            String facultyName = allTeachers[i].getFaculty() != null ?
                    allTeachers[i].getFaculty().getShortName() : "немає";
            System.out.println((i + 1) + ". [ID: " + allTeachers[i].getId() + "] " +
                    allTeachers[i].getFullName() + " - " + allTeachers[i].getPosition() +
                    " (" + facultyName + ")");
        }

        System.out.println("\nОберіть метод пошуку:");
        System.out.println("1. По ID");
        System.out.println("2. По повному імені");
        System.out.print("Ваш вибір (1 або 2): ");

        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Невірний формат!");
            return;
        }

        Teacher teacher = null;

        switch (choice) {
            case 1:
                System.out.print("Введіть ID викладача: ");
                try {
                    int searchId = Integer.parseInt(scanner.nextLine().trim());
                    Optional<Teacher> teacherOpt = teacherRepository.findById(searchId);
                    if (teacherOpt.isPresent()) {
                        teacher = teacherOpt.get();
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
                Optional<Teacher> teacherOpt = teacherRepository.findByFullName(fullName);
                if (teacherOpt.isPresent()) {
                    teacher = teacherOpt.get();
                }
                break;

            default:
                System.err.println("Невірний вибір!");
                return;
        }

        if (teacher == null) {
            System.err.println("Викладача не знайдено!");
            return;
        }

        boolean deleted = teacherRepository.deleteTeacherById(teacher.getId());
        if (deleted) {
            System.out.println("Викладача " + teacher.getFullName() + " успішно видалено!");
        } else {
            System.err.println("Помилка при видаленні!");
        }
    }


    /////////////////////////
    /// UPDATE OPERATIONS ///
    /////////////////////////

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

    public void updateUniversity() {
        System.out.println("---Редагувати інформацію про університет---");

        University currentUniversity = universityRepository.getUniversity();

        System.out.println("\nПоточна інформація:");
        System.out.println(currentUniversity.toString());

        System.out.println("\n--- Введіть нові дані (введіть 's' щоб залишити поточне значення) ---");

        String newFullName = "";
        do {
            System.out.print("Повна назва [" + currentUniversity.getFullName() + "]: ");
            newFullName = scanner.nextLine().trim();

            if (newFullName.equalsIgnoreCase("s") || newFullName.isEmpty()) {
                newFullName = currentUniversity.getFullName();
                break;
            }

            if (inputValidator.checkWord(newFullName).equals("-1")) {
                System.out.println("Невірна назва. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newFullName).equals("-1"));

        String newShortName = "";
        do {
            System.out.print("Скорочена назва [" + currentUniversity.getShortName() + "]: ");
            newShortName = scanner.nextLine().trim();

            if (newShortName.equalsIgnoreCase("s") || newShortName.isEmpty()) {
                newShortName = currentUniversity.getShortName();
                break;
            }

            if (newShortName.isEmpty()) {
                System.out.println("Скорочена назва не може бути порожньою.");
            }
        } while (newShortName.isEmpty());

        String newCity = "";
        do {
            System.out.print("Місто [" + currentUniversity.getCity() + "]: ");
            newCity = scanner.nextLine().trim();

            if (newCity.equalsIgnoreCase("s") || newCity.isEmpty()) {
                newCity = currentUniversity.getCity();
                break;
            }

            if (inputValidator.checkWord(newCity).equals("-1")) {
                System.out.println("Невірне місто. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newCity).equals("-1"));

        String newAddress = "";
        do {
            System.out.print("Адреса [" + currentUniversity.getAddress() + "]: ");
            newAddress = scanner.nextLine().trim();

            if (newAddress.equalsIgnoreCase("s") || newAddress.isEmpty()) {
                newAddress = currentUniversity.getAddress();
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

    public void updateFaculty() {
        System.out.println("---Редагувати факультет---");

        Faculty[] faculties = facultyRepository.getFaculties();
        if (faculties.length == 0) {
            System.err.println("Немає факультетів для редагування!");
            return;
        }

        System.out.println("\n=== Список факультетів ===");
        for (int i = 0; i < faculties.length; i++) {
            String deanName = faculties[i].getDean() != null ?
                    faculties[i].getDean().getFullName() : "не призначено";
            System.out.println((i + 1) + ". [Код: " + faculties[i].getCode() + "] " +
                    faculties[i].getShortName() + " - " + faculties[i].getName() +
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
            Teacher[] allTeachers = teacherRepository.getTeachers();
            if (allTeachers.length == 0) {
                System.err.println("Немає викладачів!");
            } else {
                boolean deanSelected = false;

                while (!deanSelected) {
                    System.out.println("\n=== Оберіть декана зі списку викладачів ===");
                    for (int i = 0; i < allTeachers.length; i++) {
                        System.out.println((i + 1) + ". [ID: " + allTeachers[i].getId() + "] " +
                                allTeachers[i].getFullName() + " - " + allTeachers[i].getPosition());
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
                        } else if (deanChoice >= 1 && deanChoice <= allTeachers.length) {
                            newDean = allTeachers[deanChoice - 1];
                            deanSelected = true;
                        } else {
                            System.err.println("Невірний вибір! Оберіть номер від 0 до " + allTeachers.length);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Невірний формат! Введіть число.");
                    }
                }
            }
        }

        Faculty updatedFaculty = new Faculty(code, newName, newShortName, newDean, newContacts);

        boolean updated = facultyRepository.updateFaculty(code, updatedFaculty);

        if (updated) {
            System.out.println("Факультет успішно оновлено!");
            if (newDean != null) {
                System.out.println("   Декан: " + newDean.getFullName());
            }
        } else {
            System.err.println("Помилка при оновленні!");
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

    public void updateTeacher() {
        System.out.println("---Редагувати викладача---");

        Teacher[] allTeachers = teacherRepository.getTeachers();
        showTeachers(allTeachers);

        System.out.println("\nОберіть метод пошуку:");
        System.out.println("1. По ID");
        System.out.println("2. По повному імені");
        System.out.print("Ваш вибір (1 або 2): ");

        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Невірний формат!");
            return;
        }

        Teacher currentTeacher = null;

        switch (choice) {
            case 1:
                System.out.print("Введіть ID викладача: ");
                try {
                    int searchId = Integer.parseInt(scanner.nextLine().trim());
                    Optional<Teacher> teacherOpt = teacherRepository.findById(searchId);
                    if (teacherOpt.isPresent()) {
                        currentTeacher = teacherOpt.get();
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
                Optional<Teacher> teacherOpt = teacherRepository.findByFullName(fullName);
                if (teacherOpt.isPresent()) {
                    currentTeacher = teacherOpt.get();
                }
                break;

            default:
                System.err.println("Невірний вибір!");
                return;
        }

        if (currentTeacher == null) {
            System.err.println("Викладача не знайдено!");
            return;
        }

        System.out.println("\n--- Редагування викладача (введіть 's' щоб залишити поточне значення) ---");

        int id = currentTeacher.getId();

        String newFirstName = "";
        do {
            System.out.print("Ім'я [" + currentTeacher.getFirstName() + "]: ");
            newFirstName = scanner.nextLine().trim();

            if (newFirstName.equalsIgnoreCase("s") || newFirstName.isEmpty()) {
                newFirstName = currentTeacher.getFirstName();
                break;
            }

            if (inputValidator.checkWord(newFirstName).equals("-1")) {
                System.out.println("Невірне ім'я. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newFirstName).equals("-1"));

        String newMiddleName = "";
        do {
            System.out.print("По батькові [" + currentTeacher.getMiddleName() + "]: ");
            newMiddleName = scanner.nextLine().trim();

            if (newMiddleName.equalsIgnoreCase("s") || newMiddleName.isEmpty()) {
                newMiddleName = currentTeacher.getMiddleName();
                break;
            }

            if (inputValidator.checkWord(newMiddleName).equals("-1")) {
                System.out.println("Невірне по батькові. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newMiddleName).equals("-1"));

        String newLastName = "";
        do {
            System.out.print("Прізвище [" + currentTeacher.getLastName() + "]: ");
            newLastName = scanner.nextLine().trim();

            if (newLastName.equalsIgnoreCase("s") || newLastName.isEmpty()) {
                newLastName = currentTeacher.getLastName();
                break;
            }

            if (inputValidator.checkWord(newLastName).equals("-1")) {
                System.out.println("Невірне прізвище. Використовуйте тільки літери або 's'.");
            }
        } while (inputValidator.checkWord(newLastName).equals("-1"));

        String newBirthDate = "";
        do {
            System.out.print("Дата народження [" + currentTeacher.getBirthDate() + "] (DD.MM.YYYY): ");
            newBirthDate = scanner.nextLine().trim();

            if (newBirthDate.equalsIgnoreCase("s") || newBirthDate.isEmpty()) {
                newBirthDate = currentTeacher.getBirthDate();
                break;
            }

            newBirthDate = inputValidator.checkFullDate(newBirthDate, 1920, 2007);
            if (newBirthDate.equals("-1")) {
                System.out.println("Невірна дата. Формат: DD.MM.YYYY (рік від 1920 до 2007) або 's'.");
            }
        } while (newBirthDate.equals("-1"));

        String newEmail = "";
        do {
            System.out.print("Email [" + currentTeacher.getEmail() + "]: ");
            newEmail = scanner.nextLine().trim();

            if (newEmail.equalsIgnoreCase("s") || newEmail.isEmpty()) {
                newEmail = currentTeacher.getEmail();
                break;
            }

            newEmail = inputValidator.checkEmail(newEmail);
            if (newEmail.equals("-1")) {
                System.out.println("Невірний email. Приклад: example@ukma.edu.ua або 's'.");
            }
        } while (newEmail.equals("-1"));

        String newPhoneNumber = "";
        do {
            System.out.print("Телефон [" + currentTeacher.getPhoneNumber() + "] (+380XXXXXXXXX): ");
            newPhoneNumber = scanner.nextLine().trim();

            if (newPhoneNumber.equalsIgnoreCase("s") || newPhoneNumber.isEmpty()) {
                newPhoneNumber = currentTeacher.getPhoneNumber();
                break;
            }

            newPhoneNumber = inputValidator.checkPhoneNumber(newPhoneNumber);
            if (newPhoneNumber.equals("-1")) {
                System.out.println("Невірний номер телефону. Формат: +380XXXXXXXXX або 's'.");
            }
        } while (newPhoneNumber.equals("-1"));

        String newPosition = "";
        do {
            System.out.print("Посада [" + currentTeacher.getPosition() + "] (професор/доцент/асистент/викладач): ");
            newPosition = scanner.nextLine().trim();

            if (newPosition.equalsIgnoreCase("s") || newPosition.isEmpty()) {
                newPosition = currentTeacher.getPosition();
                break;
            }

            if (newPosition.isEmpty()) {
                System.out.println("Посада не може бути порожньою.");
            }
        } while (newPosition.isEmpty());

        String newAcademicDegree = "";
        do {
            System.out.print("Науковий ступінь [" + currentTeacher.getAcademicDegree() + "] (доктор наук/доктор філософії): ");
            String degreeInput = scanner.nextLine().trim();

            if (degreeInput.equalsIgnoreCase("s") || degreeInput.isEmpty()) {
                newAcademicDegree = currentTeacher.getAcademicDegree();
                break;
            }

            newAcademicDegree = inputValidator.checkAcademicDegree(degreeInput);
            if (newAcademicDegree.equals("-1")) {
                System.out.println("Невірний науковий ступінь. Оберіть: 'доктор наук' або 'доктор філософії' або 's'.");
            }
        } while (newAcademicDegree.equals("-1"));

        String newAcademicTitle = "";
        do {
            System.out.print("Вчене звання [" + currentTeacher.getAcademicTitle() + "] (професор/доцент/старший дослідник): ");
            String titleInput = scanner.nextLine().trim();

            if (titleInput.equalsIgnoreCase("s") || titleInput.isEmpty()) {
                newAcademicTitle = currentTeacher.getAcademicTitle();
                break;
            }

            newAcademicTitle = inputValidator.checkAcademicTitle(titleInput);
            if (newAcademicTitle.equals("-1")) {
                System.out.println("Невірне вчене звання. Оберіть: 'професор', 'доцент' або 'старший дослідник' або 's'.");
            }
        } while (newAcademicTitle.equals("-1"));

        String newEmploymentDate = "";
        do {
            System.out.print("Дата прийняття на роботу [" + currentTeacher.getEmploymentDate() + "] (DD.MM.YYYY): ");
            newEmploymentDate = scanner.nextLine().trim();

            if (newEmploymentDate.equalsIgnoreCase("s") || newEmploymentDate.isEmpty()) {
                newEmploymentDate = currentTeacher.getEmploymentDate();
                break;
            }

            newEmploymentDate = inputValidator.checkFullDate(newEmploymentDate, 1992);
            if (newEmploymentDate.equals("-1")) {
                System.out.println("Невірна дата. Формат: DD.MM.YYYY (рік від 1992 до 2026) або 's'.");
            }
        } while (newEmploymentDate.equals("-1"));

        double newWorkload = 0.0;
        boolean validWorkload = false;
        do {
            try {
                System.out.print("Навантаження [" + currentTeacher.getWorkload() + " годин]: ");
                String workloadInput = scanner.nextLine().trim();

                if (workloadInput.equalsIgnoreCase("s") || workloadInput.isEmpty()) {
                    newWorkload = currentTeacher.getWorkload();
                    validWorkload = true;
                } else {
                    newWorkload = Double.parseDouble(workloadInput);
                    if (newWorkload < 0) {
                        System.out.println("Навантаження не може бути від'ємним.");
                    } else if (newWorkload > 1000) {
                        System.out.println("Навантаження занадто велике (макс. 1000 годин).");
                    } else {
                        validWorkload = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число або 's'.");
            }
        } while (!validWorkload);

        System.out.println("\nПоточний факультет: " +
                (currentTeacher.getFaculty() != null ? currentTeacher.getFaculty().getShortName() : "не вказано"));
        System.out.println("Поточна кафедра: " +
                (currentTeacher.getDepartment() != null ? currentTeacher.getDepartment().getName() : "не вказано"));

        System.out.print("\nЗмінити кафедру? (y/n): ");
        String changeDept = scanner.nextLine().trim();

        Department selectedDepartment = currentTeacher.getDepartment();

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

        Teacher updatedTeacher = new Teacher(id, newFirstName, newMiddleName, newLastName, newBirthDate,
                newEmail, newPhoneNumber, newPosition, selectedDepartment, newAcademicDegree,
                newAcademicTitle, newEmploymentDate, newWorkload);

        boolean updated = teacherRepository.updateTeacher(id, updatedTeacher);

        if (updated) {
            System.out.println("\nВикладача успішно оновлено!");
        } else {
            System.err.println("\nПомилка при оновленні!");
        }
    }
}