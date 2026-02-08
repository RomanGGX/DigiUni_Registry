package ua.university.service;

import ua.university.repository.StudentRepository;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputProcessor {

    /** Adds find operations to the class */
    private final FindOperations findOperations;
    /** Adds input scanner to the class */
    private final Scanner scanner = new Scanner(System.in);

    private final CRUDOperations crudOperations;
    private final StudentRepository studentRepository;

    public InputProcessor(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.crudOperations = new CRUDOperations(studentRepository);
        this.findOperations = new FindOperations(studentRepository);
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
                    7 - вихід
                    """, 7)) {
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
                    running = false;
                    break;
            }
        }
    }

    /** Interacts with university */
    private void processUniversity() {
        if  (parseRequest("""
                
                Оберіть дію:
                1 - інформація про заклад
                2 - повернутися
                """, 2) == 1) ; // University toString method
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
                // Faculty table method
                break;
            case 2:
                // Faculty add method
                break;
            case 3:
                // Faculty delete method
                break;
            case 4:
                // Faculty redact method
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
                // Department table method
                break;
            case 2:
                // Department add method
                break;
            case 3:
                // Department delete method
                break;
            case 4:
                // Department redact method
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
                // Student move method
                break;
            case 6:
                // Student change course method
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
                // Teacher table method
                break;
            case 2:
                // Teacher add method
                break;
            case 3:
                // Teacher delete method
                break;
            case 4:
                // Teacher redact method
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
