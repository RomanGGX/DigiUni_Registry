package ua.university.ui;

import ua.university.exceptions.EmptyInputException;
import ua.university.exceptions.UnsupportedIntegerInputException;
import ua.university.repository.*;
import ua.university.service.*;

import java.util.*;

public class InputProcessor {

    /** Adds find operations to the class */
    private final FindOperations findOperations;
    /** Adds input scanner to the class */
    private final Scanner scanner = new Scanner(System.in);
    /** Adds access level */
    private int accessLevel = 0;
    /** Saves information about if the program is running */
    private boolean running = true;

    private final UniversityOperations universityOperations;
    private final StudentOperations studentOperations;
    private final FacultyOperations facultyOperations;
    private final TeacherOperations teacherOperations;
    private final DepartmentOperations departmentOperations;
    private final StudentRepository studentRepository;
    private final UniversityRepository universityRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final TransferOperations transferOperations;
    private final ReportOperations reportOperations;
    private final UserOperations userOperations;

    public InputProcessor(StudentRepository studentRepository, UniversityRepository universityRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.universityRepository = universityRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.reportOperations = new ReportOperations(studentRepository, teacherRepository, facultyRepository, departmentRepository);
        this.transferOperations = new TransferOperations(studentRepository, universityRepository, facultyRepository, departmentRepository);
        this.findOperations = new FindOperations(studentRepository, universityRepository);
        this.userOperations = new UserOperations(userRepository);
        this.studentOperations = new StudentOperations(studentRepository, facultyRepository, departmentRepository);
        this.teacherOperations = new TeacherOperations(facultyRepository, departmentRepository, teacherRepository);
        this.facultyOperations = new FacultyOperations(facultyRepository, teacherRepository);
        this.departmentOperations = new DepartmentOperations(facultyRepository, departmentRepository, teacherRepository);
        this.universityOperations = new UniversityOperations(universityRepository);
    }

    /** Defines what to interact with */
    public void defineObject () {

        List<CommandContainer> mainCommands = new ArrayList<>(List.of(
                new CommandContainer("університет", this::processUniversity, CommandContainer.GUEST_ACCESS),
                new CommandContainer("факультети", this::processFaculty, CommandContainer.GUEST_ACCESS),
                new CommandContainer("кафедри", this::processDepartment, CommandContainer.GUEST_ACCESS),
                new CommandContainer("студенти", this::processStudent, CommandContainer.USER_ACCESS),
                new CommandContainer("викладачі", this::processTeacher, CommandContainer.USER_ACCESS),
                new CommandContainer("пошук студентів", this::processFind, CommandContainer.USER_ACCESS),
                new CommandContainer("звіти", this::processReports, CommandContainer.USER_ACCESS),
                new CommandContainer("вхід", this::authorize, CommandContainer.GUEST_ACCESS),
                new CommandContainer("дії з користувачами", this::processUser, CommandContainer.ADMIN_ACCESS)
        ));

        while (running) {
            proceedOption("Оберіть, з чим працювати:", "вихід", mainCommands, true);
            Thread initializationThread = new Thread(() -> Initializer.connectRepositories(studentRepository, facultyRepository, departmentRepository, teacherRepository), "Update thread");
            initializationThread.start();
        }
    }

    /** Interacts with university */
    private void processUniversity() {
        List<CommandContainer> universityCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про університети", universityOperations::showUniversity, CommandContainer.GUEST_ACCESS),
                new CommandContainer("редагувати інформацію про університет", universityOperations::updateUniversity, CommandContainer.MANAGER_ACCESS)
        ));

        proceedOption("Оберіть дію:", "повернутися", universityCommands);
    }

    /** interacts with faculties */
    private void processFaculty() {
        List<CommandContainer> facultyCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про факультет", () -> facultyOperations.showFaculties(facultyRepository.findAll()), CommandContainer.GUEST_ACCESS),
                new CommandContainer("створити факультет", facultyOperations::addFaculty, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("видалити факультет", facultyOperations::deleteFaculty, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("редагуфати факультет", facultyOperations::updateFaculty, CommandContainer.MANAGER_ACCESS)
        ));

        proceedOption("Оберіть дію:", "певернутися", facultyCommands);
    }

    /** Interacts with departments */
    private void processDepartment() {
        List<CommandContainer> departmentCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про кафедри", () -> departmentOperations.showDepartments(departmentRepository.findAll()), CommandContainer.GUEST_ACCESS),
                new CommandContainer("створити кафедру", departmentOperations::addDepartment, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("видалити кафедру", departmentOperations::deleteDepartment, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("редагуфати кафедру", departmentOperations::updateDepartment, CommandContainer.MANAGER_ACCESS)
        ));

        proceedOption("Оберіть дію:", "повернутися", departmentCommands);
    }

    /** Interacts with students */
    private void processStudent() {
        List<CommandContainer> studentCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про студентів", () -> studentOperations.showStudents(studentRepository.findAll()), CommandContainer.USER_ACCESS),
                new CommandContainer("створити студента", studentOperations::addStudent, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("видалити студента", studentOperations::deleteStudent, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("редагуфати студента", studentOperations::updateStudent, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("перевести студента", transferOperations::transferStudentToDepartment, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("змінити курс студента", transferOperations::transferStudentToCourse, CommandContainer.MANAGER_ACCESS)
        ));

        proceedOption("Оберіть дію:", "повернутися", studentCommands);
    }

    /** Interacts with teachers */
    private void processTeacher() {
        List<CommandContainer> teacherCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про викладачів", () -> teacherOperations.showTeachers(teacherRepository.findAll()), CommandContainer.USER_ACCESS),
                new CommandContainer("створити викладача", teacherOperations::addTeacher, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("видалити викладача", teacherOperations::deleteTeacher, CommandContainer.MANAGER_ACCESS),
                new CommandContainer("редагуфати викладача", teacherOperations::updateTeacher, CommandContainer.MANAGER_ACCESS)
        ));

        proceedOption("Оберіть дію:", "повернутися", teacherCommands);
    }

    /** Interacts with find operations */
    private void processFind() {
        List<CommandContainer> findCommands = new ArrayList<>(List.of(
                new CommandContainer("повне ім'я", findOperations::studentByFullName, CommandContainer.USER_ACCESS),
                new CommandContainer("курс", findOperations::studentByCourse, CommandContainer.USER_ACCESS),
                new CommandContainer("група", findOperations::studentByGroup, CommandContainer.USER_ACCESS),
                new CommandContainer("id студента", findOperations::studentByStudentID, CommandContainer.USER_ACCESS),
                new CommandContainer("електронна пошта", findOperations::studentByEmail, CommandContainer.USER_ACCESS)
        ));

        proceedOption("Оберіть параметр для пошуку:", "повернутися", findCommands);
    }

    private void processReports() {
        List<CommandContainer> departmentCommands = new ArrayList<>(List.of(
                new CommandContainer("всі студенти за курсами", reportOperations::showAllStudentsByCourse, CommandContainer.USER_ACCESS),
                new CommandContainer("студенти факультету за алфавітом", reportOperations::showStudentsByFacultyAlphabetically, CommandContainer.USER_ACCESS),
                new CommandContainer("викладачі факультету за алфавітом", reportOperations::showTeachersByFacultyAlphabetically, CommandContainer.USER_ACCESS),
                new CommandContainer("студенти кафедри за курсами", reportOperations::showStudentsByDepartmentByCourse, CommandContainer.USER_ACCESS),
                new CommandContainer("студенти кафедри за алфавітом", reportOperations::showStudentsByDepartmentAlphabetically, CommandContainer.USER_ACCESS),
                new CommandContainer("викладачі кафедри за алфавітом", reportOperations::showTeachersByDepartmentAlphabetically, CommandContainer.USER_ACCESS),
                new CommandContainer("студенти кафедри вказаного курсу", reportOperations::showStudentsByDepartmentAndCourse, CommandContainer.USER_ACCESS),
                new CommandContainer("студенти кафедри вказаного курсу (за алфавітом)", reportOperations::showStudentsByDepartmentAndCourseAlphabetically, CommandContainer.USER_ACCESS)
        ));

        proceedOption("Оберіть звіт:", "повернутися", departmentCommands);
    }

    /** Interacts with user operations */
    private void processUser() {
        List<CommandContainer> userContainer = new ArrayList<>(List.of(
                new CommandContainer("додати користовача", userOperations::addUser, CommandContainer.ADMIN_ACCESS)
        ));

        proceedOption("Оберіть дію:", "повернутися", userContainer);
    }

/////////////////////////////////////////////////////////////////////
////                     ACCESS LEVEL CHANGE                     ////
/////////////////////////////////////////////////////////////////////

    /** Allows to authorize */
    private void authorize() {
        String loginName = readNotEmpty("Введіть логін: ");
        String password = readNotEmpty("Введіть пароль: ");

        if (userRepository.checkIfExists(loginName, password)) {
            accessLevel = userRepository.getAccessLevel(loginName);

            switch (accessLevel) {
                case 1:
                    System.out.print("\nВітаємо!\nРівень доступу - користувач\n");
                    break;
                case 2:
                    System.out.print("\nВітаємо!\nРівень доступу - менеджер\n");
                    break;
                case 3:
                    System.out.print("\nВітаємо!\nРівень доступу - адміністратор\n");
                    break;
            }
        }
        else System.out.println("\nНеправильний логін або пароль\n");
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

/////////////////////////////////////////////////////////////////////
////                     OPTIONS CHOOSE SYSTEM                   ////
/////////////////////////////////////////////////////////////////////

    /**
     * Output the options list and allows to choose an option
     * @param firstRowText String text before the list
     * @param returnRowText String return option text
     * @param listToProceed CommandContainer list of options to output
     * @param isFirst boolean. Set "true" to exit the program on return option
     */
    private void proceedOption(String firstRowText, String returnRowText, List<CommandContainer> listToProceed, boolean isFirst) {
        int counter = 1;
        Map<Integer, Runnable> commandsMap = new HashMap<>();

        System.out.println("\n" + firstRowText);

        for (CommandContainer currentCommand : listToProceed) {
            if (currentCommand.isAvailable(accessLevel)) {
                System.out.println(counter + " - " + currentCommand.message);
                commandsMap.put(counter, currentCommand.action);
                counter++;
            }
        }

        System.out.println(counter + " - " + returnRowText + "\n");
        int userInput = getIntInput(counter);

        if (userInput != counter) commandsMap.get(userInput).run();
        else if (isFirst) running = false;
    }

    /**
     * *OVERLOADING* Output the options list and allows to choose an option. Return does not finish the program
     * @param firstRowText String text before the list
     * @param returnRowText String return option text
     * @param listToProceed CommandContainer list of options to output
     */
    private void proceedOption(String firstRowText, String returnRowText, List<CommandContainer> listToProceed) {
        proceedOption(firstRowText, returnRowText, listToProceed, false);
    }

/////////////////////////////////////////////////////////////////////
////                       INT PARSE SYSTEM                      ////
/////////////////////////////////////////////////////////////////////

    private int getIntInput(int optionsNumber) {
        int result = 0;
        boolean resultApproved = false;

        do {
            try {
                String stringResult = scanner.nextLine();

                if (stringResult.isEmpty()) throw new EmptyInputException("Empty input");
                else result = Integer.parseInt(stringResult);

                if (result < 1 || result > optionsNumber) throw new UnsupportedIntegerInputException("Unsupported number");
                resultApproved = true;
            } catch (UnsupportedIntegerInputException | NumberFormatException ex) {
                System.out.println("Введіть коректне значення (1-" + optionsNumber + ")");
            } catch (EmptyInputException ex) {
                System.out.println("Порожнє введення");
            }
        } while (!resultApproved);

        return result;
    }

    private record CommandContainer(String message, Runnable action, int accessLevel) {
        public static final int GUEST_ACCESS = 0;
        public static final int USER_ACCESS = 0b0001;
        public static final int MANAGER_ACCESS = 0b0010;
        public static final int ADMIN_ACCESS = 0b0100;

        /**
         * Checks if access level of command is lower than arg
         * @param arg int access level
         * @return boolean result of arg >= accessLevel operation
         */
        public boolean isAvailable(int arg) {
            return (arg & accessLevel) == accessLevel;
        }
    }
}
