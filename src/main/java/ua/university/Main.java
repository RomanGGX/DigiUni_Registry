package ua.university;
import ua.university.domain.University;
import ua.university.repository.*;
import ua.university.service.InputProcessor;

public class Main {

    private static final UniversityRepository universityRepository = new UniversityRepository();
    private static final FacultyRepository facultyRepository = new FacultyRepository();
    private static final DepartmentRepository departmentRepository = new DepartmentRepository();
    private static final StudentRepository studentRepository = new StudentRepository();
    private static final TeacherRepository teacherRepository = new TeacherRepository();

    private static final InputProcessor inputProcessor = new InputProcessor(studentRepository, universityRepository, facultyRepository, departmentRepository, teacherRepository);

    /** Adds a list of access levels */
    private enum AccessLevel {user, manager, admin}
    /** Utilizes the list of access levels */
    private static AccessLevel accessLevel = AccessLevel.manager;

    public static void main(String[] args) {
        Initializer.initializeAll(studentRepository, facultyRepository, departmentRepository, teacherRepository);

        System.out.println();
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   Вас вітає система DigiUni Registry!      ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();

        inputProcessor.defineObject();
    }
}