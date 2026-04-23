package ua.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Teacher;
import ua.university.repository.*;

import java.util.*;

public class TeacherOperations {
    private final Scanner scanner = new Scanner(System.in);
    private final InputValidator inputValidator = new InputValidator();
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private static final Logger logger = LoggerFactory.getLogger(TeacherOperations.class);

    public TeacherOperations(FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository) {
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
    }

    /*
    Displays teachers list
    */
    public void showTeachers(List<Teacher> teachers) {
        System.out.println("\n=== Список викладачів ===");

        if (teachers.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }

        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
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
            if (teacherRepository.findByEmail(email).isPresent()) {
                System.out.println("Викладач з таким email вже існує. Введіть інший.");
                email = "";
            }
        } while (email.isEmpty() || email.equals("-1"));

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

        List<Faculty> faculties = facultyRepository.findAll();
        if (faculties.isEmpty()) {
            System.err.println("Немає факультетів!");
            return;
        }

        Faculty selectedFaculty = null;
        boolean facultySelected = false;

        while (!facultySelected) {
            System.out.println("\n=== Оберіть факультет ===");
            for (int i = 0; i < faculties.size(); i++) {
                System.out.println((i + 1) + ". " + faculties.get(i).getShortName() + " - " + faculties.get(i).getName());
            }

            System.out.print("Номер факультету: ");
            String facultyInput = scanner.nextLine().trim();

            if (facultyInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім! Спробуйте ще раз.");
                continue;
            }

            try {
                int facultyChoice = Integer.parseInt(facultyInput);
                if (facultyChoice < 1 || facultyChoice > faculties.size()) {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + faculties.size());
                    continue;
                }
                selectedFaculty = faculties.get(facultyChoice - 1);
                facultySelected = true;
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
            System.out.println("\n=== Оберіть кафедру ===");
            for (int i = 0; i < departments.size(); i++) {
                System.out.println((i + 1) + ". " + departments.get(i).getName());
            }

            System.out.print("Номер кафедри: ");
            String deptInput = scanner.nextLine().trim();

            if (deptInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім! Спробуйте ще раз.");
                continue;
            }

            try {
                int deptChoice = Integer.parseInt(deptInput);
                if (deptChoice < 1 || deptChoice > departments.size()) {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + departments.size());
                    continue;
                }
                selectedDepartment = departments.get(deptChoice - 1);
                departmentSelected = true;
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        Teacher newTeacher = new Teacher(id, firstName, middleName, lastName, birthDate, email,
                phoneNumber, position, selectedDepartment, academicDegree, academicTitle,
                employmentDate, workload);

        teacherRepository.add(newTeacher);
        logger.info("New Teacher '{}' was added successfully", newTeacher.getFullName());

        System.out.println("\nВикладача успішно додано!");
        System.out.println("\n---Інформація про створеного викладача---");
        System.out.println(newTeacher.toString());
    }


    public void deleteTeacher() {
        System.out.println("---Видалити викладача---");

        List<Teacher> allTeachers = teacherRepository.findAll();
        if (allTeachers.isEmpty()) {
            System.err.println("Немає викладачів для видалення!");
            return;
        }

        System.out.println("\n=== Список викладачів ===");
        for (int i = 0; i < allTeachers.size(); i++) {
            String facultyName = allTeachers.get(i).getFaculty() != null ?
                    allTeachers.get(i).getFaculty().getShortName() : "немає";
            System.out.println((i + 1) + ". [ID: " + allTeachers.get(i).getId() + "] " +
                    allTeachers.get(i).getFullName() + " - " + allTeachers.get(i).getPosition() +
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

        boolean deleted = teacherRepository.deleteById(teacher.getId());
        if (deleted) {
            System.out.println("Викладача " + teacher.getFullName() + " успішно видалено!");
            logger.info("Teacher '{}' was deleted successfully", teacher.getFullName());
        } else {
            System.err.println("Помилка при видаленні!");
        }
    }

    public void updateTeacher() {
        System.out.println("---Редагувати викладача---");

        List<Teacher> allTeachers = teacherRepository.findAll();
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
            List<Faculty> faculties = facultyRepository.findAll();
            if (faculties.isEmpty()) {
                System.err.println("Немає факультетів!");
            } else {
                Faculty selectedFaculty = null;
                boolean facultySelected = false;

                while (!facultySelected) {
                    System.out.println("\n=== Оберіть факультет ===");
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
                } else {
                    boolean departmentSelected = false;

                    while (!departmentSelected) {
                        System.out.println("\n=== Оберіть кафедру ===");
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
                }
            }
        }

        Teacher updatedTeacher = new Teacher(id, newFirstName, newMiddleName, newLastName, newBirthDate,
                newEmail, newPhoneNumber, newPosition, selectedDepartment, newAcademicDegree,
                newAcademicTitle, newEmploymentDate, newWorkload);

        boolean updated = teacherRepository.update(id, updatedTeacher);

        if (updated) {
            System.out.println("\nВикладача успішно оновлено!");
            logger.info("Teacher '{}' information was updated successfully", currentTeacher.getFullName());
        } else {
            System.err.println("\nПомилка при оновленні!");
        }
    }
}
