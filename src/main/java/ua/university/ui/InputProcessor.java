package ua.university.ui;

import ua.university.repository.*;
import ua.university.service.CRUDOperations;
import ua.university.service.FindOperations;
import ua.university.service.ReportOperations;
import ua.university.service.TransferOperations;

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

    private final CRUDOperations crudOperations;
    private final StudentRepository studentRepository;
    private final UniversityRepository universityRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final TransferOperations transferOperations;
    private final ReportOperations reportOperations;

    public InputProcessor(StudentRepository studentRepository, UniversityRepository universityRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.universityRepository = universityRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
        this.reportOperations = new ReportOperations(studentRepository, teacherRepository, facultyRepository, departmentRepository);
        this.transferOperations = new TransferOperations(studentRepository, universityRepository, facultyRepository, departmentRepository);
        this.crudOperations = new CRUDOperations(studentRepository, universityRepository, facultyRepository, departmentRepository, teacherRepository);
        this.findOperations = new FindOperations(studentRepository, universityRepository);
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
                new CommandContainer("звіти", this::processReports, 1)
        ));

        while (running) {
            proceedOption("Оберіть, з чим працювати:", "вихід", mainCommands, true);
        }
    }

    /** Interacts with university */
    private void processUniversity() {
        List<CommandContainer> universityCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про університети", crudOperations::showUniversity, 1),
                new CommandContainer("редагувати інформацію про університет", crudOperations::updateUniversity, 2)
        ));

        proceedOption("Оберіть дію:", "повернутися", universityCommands);
    }

    /** interacts with faculties */
    private void processFaculty() {
        List<CommandContainer> facultyCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про факультет", () -> crudOperations.showFaculties(facultyRepository.getFaculties()), 1),
                new CommandContainer("створити факультет", crudOperations::addFaculty, 2),
                new CommandContainer("видалити факультет", crudOperations::deleteFaculty, 2),
                new CommandContainer("редагуфати факультет", crudOperations::updateFaculty, 2)
        ));

        proceedOption("Оберіть дію:", "певернутися", facultyCommands);
    }

    /** Interacts with departments */
    private void processDepartment() {
        List<CommandContainer> departmentCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про кафедри", () -> crudOperations.showDepartments(departmentRepository.getDepartments()), 1),
                new CommandContainer("створити кафедру", crudOperations::addDepartment, 2),
                new CommandContainer("видалити кафедру", crudOperations::deleteDepartment, 2),
                new CommandContainer("редагуфати кафедру", crudOperations::updateDepartment, 2)
        ));

        proceedOption("Оберіть дію:", "повернутися", departmentCommands);
    }

    /** Interacts with students */
    private void processStudent() {
        List<CommandContainer> studentCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про студентів", () -> crudOperations.showStudents(studentRepository.getStudents()), 1),
                new CommandContainer("створити студента", crudOperations::addStudent, 2),
                new CommandContainer("видалити студента", crudOperations::deleteStudent, 2),
                new CommandContainer("редагуфати студента", crudOperations::updateStudent, 2),
                new CommandContainer("перевести студента", transferOperations::transferStudentToDepartment, 2),
                new CommandContainer("змінити курс студента", transferOperations::transferStudentToCourse, 2)
        ));

        proceedOption("Оберіть дію:", "повернутися", studentCommands);
    }

    /** Interacts with teachers */
    private void processTeacher() {
        List<CommandContainer> teacherCommands = new ArrayList<>(List.of(
                new CommandContainer("інформація про викладачів", () -> crudOperations.showTeachers(teacherRepository.getTeachers()), 1),
                new CommandContainer("створити викладача", crudOperations::addTeacher, 2),
                new CommandContainer("видалити викладача", crudOperations::deleteTeacher, 2),
                new CommandContainer("редагуфати викладача", crudOperations::updateTeacher, 2)
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

                if (stringResult.isEmpty()) throw new InputMismatchException();
                else result = Integer.parseInt(stringResult);

                if (result < 1 || result > optionsNumber) throw new IllegalArgumentException();
                resultApproved = true;
            } catch (IllegalArgumentException ex) {
                System.out.println("Введіть коректне значення (1-" + optionsNumber + ")");
            } catch (InputMismatchException ex) {
                System.out.println("Порожнє введення");
            }
        } while (!resultApproved);

        return result;
    }
}
