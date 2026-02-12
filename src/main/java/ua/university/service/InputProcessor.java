package ua.university.service;

import ua.university.repository.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputProcessor {

    /** Adds find operations to the class */
    private final FindOperations findOperations;
    /** Adds input scanner to the class */
    private final Scanner scanner = new Scanner(System.in);

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
        boolean running = true;

        while (running) {
            switch (parseRequest("""
                    
                    Оберіть, з чим працювати:
                    1 - університет
                    2 - факультети
                    3 - кафедри
                    4 - студенти
                    5 - викладачі
                    6 - пошук студентів
                    7 - звіти
                    8 - вихід
                    """, 8)) {
                case 1:
                    processUniversity();
                    break;
                case 2:
                    processFaculty();
                    break;
                case 3:
                    processDepartment();
                    break;
                case 4:
                    processStudent();
                    break;
                case 5:
                    processTeacher();
                    break;
                case 6:
                    processFind();
                    break;
                case 7:
                    processReports();
                    break;
                case 8:
                    running = false;
                    break;
            }
        }
    }

    /** Interacts with university */
    private void processUniversity() {
        switch (parseRequest("""
                
                Оберіть дію:
                1 - інформація про університети
                2 - редагувати інформацію про університет
                3 - повернутися
                """, 3)) {
            case 1:
                crudOperations.showUniversity();
                break;
            case 2:
                crudOperations.updateUniversity();
                break;
        }
    }

    /** interacts with faculties */
    private void processFaculty() {
        switch (parseRequest("""
                
                Оберіть дію:
                1 - інформація про факультети
                2 - створити факультет
                3 - видалити факультет
                4 - редагувати факультет
                5 - повернутися
                """, 5)) {
            case 1:
                crudOperations.showFaculties(facultyRepository.getFaculties());
                break;
            case 2:
                crudOperations.addFaculty();
                break;
            case 3:
                crudOperations.deleteFaculty();
                break;
            case 4:
                crudOperations.updateFaculty();
                break;
        }
    }

    /** Interacts with departments */
    private void processDepartment() {
        switch (parseRequest("""
                
                Оберіть дію:
                1 - інформація про кафедри
                2 - створити кафедру
                3 - видалити кафедру
                4 - редагувати кафедру
                5 - повернутися
                """, 5)) {
            case 1:
                crudOperations.showDepartments(departmentRepository.getDepartments());
                break;
            case 2:
                crudOperations.addDepartment();
                break;
            case 3:
                crudOperations.deleteDepartment();
                break;
            case 4:
                crudOperations.updateDepartment();
                break;
        }
    }

    /** Interacts with students */
    private void processStudent() {
        switch (parseRequest("""
                
                Оберіть дію:
                1 - інформація про студентів
                2 - створити студента
                3 - видалити студента
                4 - редагувати студента
                5 - перевести студента
                6 - змінити курс студента
                7 - повернутися
                """, 7)) {
            case 1:
                crudOperations.showStudents(studentRepository.getStudents());
                break;
            case 2:
                crudOperations.addStudent();
                break;
            case 3:
                crudOperations.deleteStudent();
                break;
            case 4:
                crudOperations.updateStudent();
                break;
            case 5:
                transferOperations.transferStudentToDepartment();
                break;
            case 6:
                transferOperations.transferStudentToCourse();
                break;
        }
    }

    /** Interacts with teachers */
    private void processTeacher() {
        switch (parseRequest("""
                
                Оберіть дію:
                1 - інформація про викладачів
                2 - створити викладача
                3 - видалити викладача
                4 - редагувати викладача
                5 - повернутися
                """, 5)) {
            case 1:
                crudOperations.showTeachers(teacherRepository.getTeachers());
                break;
            case 2:
                crudOperations.addTeacher();
                break;
            case 3:
                crudOperations.deleteTeacher();
                break;
            case 4:
                crudOperations.updateTeacher();
                break;
        }
    }

    /** Interacts with find operations */
    private void processFind() {
        switch (parseRequest("""
                
                Оберіть параметр для пошуку:
                1 - повне ім'я
                2 - курс
                3 - група
                4 - id студента
                5 - електронна пошта
                6 - повернутися
                """, 6)) {
            case 1:
                findOperations.studentByFullName();
                break;
            case 2:
                findOperations.studentByCourse();
                break;
            case 3:
                findOperations.studentByGroup();
                break;
            case 4:
                findOperations.studentByStudentID();
                break;
            case 5:
                findOperations.studentByEmail();
                break;
        }
    }

    private void processReports() {
        switch (parseRequest("""
            
            Оберіть звіт:
            1 - всі студенти за курсами
            2 - студенти факультету за алфавітом
            3 - викладачі факультету за алфавітом
            4 - студенти кафедри за курсами
            5 - студенти кафедри за алфавітом
            6 - викладачі кафедри за алфавітом
            7 - студенти кафедри вказаного курсу
            8 - студенти кафедри вказаного курсу (за алфавітом)
            9 - повернутися
            """, 9)) {
            case 1:
                reportOperations.showAllStudentsByCourse();
                break;
            case 2:
                reportOperations.showStudentsByFacultyAlphabetically();
                break;
            case 3:
                reportOperations.showTeachersByFacultyAlphabetically();
                break;
            case 4:
                reportOperations.showStudentsByDepartmentByCourse();
                break;
            case 5:
                reportOperations.showStudentsByDepartmentAlphabetically();
                break;
            case 6:
                reportOperations.showTeachersByDepartmentAlphabetically();
                break;
            case 7:
                reportOperations.showStudentsByDepartmentAndCourse();
                break;
            case 8:
                reportOperations.showStudentsByDepartmentAndCourseAlphabetically();
                break;
        }
    }

/////////////////////////////////////////////////////////////////////
////                       INT PARSE SYSTEM                      ////
/////////////////////////////////////////////////////////////////////

    private int parseRequest (String commandText, int optionsNumber) {
        System.out.println(commandText);
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
