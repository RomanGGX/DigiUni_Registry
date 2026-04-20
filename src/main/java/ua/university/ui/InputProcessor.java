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
    private int accessLevel = 1;
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
                new CommandContainer("університет", this::processUniversity, 1),
                new CommandContainer("факультети", this::processFaculty, 1),
                new CommandContainer("кафедри", this::processDepartment, 1),
                new CommandContainer("студенти", this::processStudent, 1),
                new CommandContainer("викладачі", this::processTeacher, 1),
                new CommandContainer("пошук студентів", this::processFind, 1),
                new CommandContainer("звіти", this::processReports, 1),
                new CommandContainer("вхід", this::authorize, 1),
                new CommandContainer("дії з користувачами", this::processUser, 3)
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
                new CommandContainer("інформація про університети", universityOperations::showUniversity, 1),
                new CommandContainer("редагувати інформацію про університет", universityOperations::updateUniversity, 2)
        ));

        proceedOption("Оберіть дію:", "повернутися", universityCommands);
    }

    /** interacts with faculties */
    private void processFaculty() {
        List<CommandContainer> facultyCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про факультет", () -> facultyOperations.showFaculties(facultyRepository.findAll()), 1),
                new CommandContainer("створити факультет", facultyOperations::addFaculty, 2),
                new CommandContainer("видалити факультет", facultyOperations::deleteFaculty, 2),
                new CommandContainer("редагуфати факультет", facultyOperations::updateFaculty, 2)
        ));

        proceedOption("Оберіть дію:", "певернутися", facultyCommands);
    }

    /** Interacts with departments */
    private void processDepartment() {
        List<CommandContainer> departmentCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про кафедри", () -> departmentOperations.showDepartments(departmentRepository.findAll()), 1),
                new CommandContainer("створити кафедру", departmentOperations::addDepartment, 2),
                new CommandContainer("видалити кафедру", departmentOperations::deleteDepartment, 2),
                new CommandContainer("редагуфати кафедру", departmentOperations::updateDepartment, 2)
        ));

        proceedOption("Оберіть дію:", "повернутися", departmentCommands);
    }

    /** Interacts with students */
    private void processStudent() {
        List<CommandContainer> studentCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про студентів", () -> studentOperations.showStudents(studentRepository.findAll()), 1),
                new CommandContainer("створити студента", studentOperations::addStudent, 2),
                new CommandContainer("видалити студента", studentOperations::deleteStudent, 2),
                new CommandContainer("редагуфати студента", studentOperations::updateStudent, 2),
                new CommandContainer("перевести студента", transferOperations::transferStudentToDepartment, 2),
                new CommandContainer("змінити курс студента", transferOperations::transferStudentToCourse, 2)
        ));

        proceedOption("Оберіть дію:", "повернутися", studentCommands);
    }

    /** Interacts with teachers */
    private void processTeacher() {
        List<CommandContainer> teacherCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про викладачів", () -> teacherOperations.showTeachers(teacherRepository.findAll()), 1),
                new CommandContainer("створити викладача", teacherOperations::addTeacher, 2),
                new CommandContainer("видалити викладача", teacherOperations::deleteTeacher, 2),
                new CommandContainer("редагуфати викладача", teacherOperations::updateTeacher, 2)
        ));

        proceedOption("Оберіть дію:", "повернутися", teacherCommands);
    }

    /** Interacts with find operations */
    private void processFind() {
        List<CommandContainer> findCommands = new ArrayList<>(List.of(
                new CommandContainer("повне ім'я", findOperations::studentByFullName, 1),
                new CommandContainer("курс", findOperations::studentByCourse, 1),
                new CommandContainer("група", findOperations::studentByGroup, 1),
                new CommandContainer("id студента", findOperations::studentByStudentID, 1),
                new CommandContainer("електронна пошта", findOperations::studentByEmail, 1)
        ));

        proceedOption("Оберіть параметр для пошуку:", "повернутися", findCommands);
    }

    private void processReports() {
        List<CommandContainer> departmentCommands = new ArrayList<>(List.of(
                new CommandContainer("всі студенти за курсами", reportOperations::showAllStudentsByCourse, 1),
                new CommandContainer("студенти факультету за алфавітом", reportOperations::showStudentsByFacultyAlphabetically, 1),
                new CommandContainer("викладачі факультету за алфавітом", reportOperations::showTeachersByFacultyAlphabetically, 1),
                new CommandContainer("студенти кафедри за курсами", reportOperations::showStudentsByDepartmentByCourse, 1),
                new CommandContainer("студенти кафедри за алфавітом", reportOperations::showStudentsByDepartmentAlphabetically, 1),
                new CommandContainer("викладачі кафедри за алфавітом", reportOperations::showTeachersByDepartmentAlphabetically, 1),
                new CommandContainer("студенти кафедри вказаного курсу", reportOperations::showStudentsByDepartmentAndCourse, 1),
                new CommandContainer("студенти кафедри вказаного курсу (за алфавітом)", reportOperations::showStudentsByDepartmentAndCourseAlphabetically, 1)
        ));

        proceedOption("Оберіть звіт:", "повернутися", departmentCommands);
    }

    /** Interacts with user operations */
    private void processUser() {
        List<CommandContainer> userContainer = new ArrayList<>(List.of(
                new CommandContainer("додати користовача", userOperations::addUser, 3)
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
                System.out.println(counter + " - " + currentCommand.getMessage());
                commandsMap.put(counter, currentCommand.getAction());
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
}
