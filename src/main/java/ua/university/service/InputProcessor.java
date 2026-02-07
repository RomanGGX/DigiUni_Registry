package ua.university.service;

import ua.university.repository.StudentRepository;

public class InputProcessor {

    /** Adds find operations to the class */
    private final FindOperations findOperations;

    private final CRUDOperations crudOperations;
    private final StudentRepository studentRepository;

    private int[] commandCode;

    public InputProcessor(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.crudOperations = new CRUDOperations(studentRepository);
        this.findOperations = new FindOperations(studentRepository);
    }

    /**
     * Defines the method to use for a command
     * @param commandCode int array code of a command
     * @param accessLevel int access level
     */
    public void defineProcess (int[] commandCode, int accessLevel) {
        this.commandCode = commandCode;

        switch (accessLevel) {
            case 0:
                defineUserCommand();
                break;
            case 1:
                defineManagerCommand();
                break;
            case 2:
                defineAdminCommand();
                break;
        }
    }

/////////////////////////////////////////////////////////////////////
////                    FIRST LEVEL OF COMMAND                   ////
/////////////////////////////////////////////////////////////////////

    private void defineUserCommand () {
        switch (commandCode[0]) {
            case 1:
                showCommand();
                break;
            case 2:
                loginCommand();
                break;
            case 3:
                exitCommand();
                break;
            case 4:
                endCommand();
                break;
            case 5:
                findCommand();
                break;
            default:
                System.err.println("Command undefined. Word: 1");
                break;
        }
    }

    private void defineManagerCommand () {
        switch (commandCode[0]) {
            case 1:
                showCommand();
                break;
            case 2:
                loginCommand();
                break;
            case 3:
                exitCommand();
                break;
            case 4:
                endCommand();
                break;
            case 5:
                findCommand();
                break;
            case 6:
                addCommand();
                break;
            case 7:
                deleteCommand();
                break;
            case 8:
                moveCommand();
                break;
            case 9:
                updateCommand();
                break;
            default:
                System.err.println("Command undefined. Word: 1");
                break;
        }
    }

    private void defineAdminCommand () {
        switch (commandCode[0]) {
            case 1:
                showCommand();
                break;
            case 2:
                loginCommand();
                break;
            case 3:
                exitCommand();
                break;
            case 4:
                endCommand();
                break;
            case 5:
                findCommand();
                break;
            case 6:
                addCommand();
                break;
            case 7:
                deleteCommand();
                break;
            case 8:
                moveCommand();
                break;
            case 9:
                updateCommand();
                break;
            case 10:
                createCommand();
                break;
            default:
                System.err.println("Command undefined. Word: 1");
                break;
        }
    }

/////////////////////////////////////////////////////////////////////
////               SECOND LEVEL OF USER COMMAND                  ////
/////////////////////////////////////////////////////////////////////

    private void showCommand () {
        switch (commandCode[1]) {
            case 11:
                // Department table method
                break;
            case 12:
                // Faculty table method
                break;
            case 13:
                // Student table method
                crudOperations.showStudents(studentRepository.getStudents());
                break;
            case 14:
                // Teacher table method
                break;
            case 15:
                // University table method
                break;
            default:
                System.err.println("Command undefined. Word: 2");
        }
    }

    private void loginCommand () {
        // Login method (unused for now)
    }

    private void exitCommand () {
        // Exit from the account method (unused for now)
    }

    private void endCommand () {
        // Program end method
    }

    private void findCommand () {
        if (commandCode[1] == 16) findByCommand();
        else System.err.println("Command undefined. Word: 2");
    }

/////////////////////////////////////////////////////////////////////
////               THIRD LEVEL OF USER COMMAND                   ////
/////////////////////////////////////////////////////////////////////

    private void findByCommand () {
        switch (commandCode[2]) {
            case 18:
                findOperations.studentByFullName();
                break;
            case 19:
                findOperations.studentByCourse();
                break;
            case 20:
                findOperations.studentByGroup();
                break;
            case 21:
                findOperations.studentByStudentID();
                break;
            case 22:
                findOperations.studentByEmail();
                break;
            default:
                System.err.println("Command undefined. Word: 3");
        }
    }

/////////////////////////////////////////////////////////////////////
////                SECOND LEVEL OF MANAGER COMMAND              ////
/////////////////////////////////////////////////////////////////////

    private void addCommand () {
        switch (commandCode[1]) {
            case 11:
                // Department add method
                break;
            case 12:
                // Faculty add method
                break;
            case 13:
                // Student add method
                crudOperations.addStudent();
                break;
            case 14:
                // Teacher add method
                break;
            default:
                System.err.println("Command undefined. Word: 2");
        }
    }

    private void deleteCommand () {
        switch (commandCode[1]) {
            case 11:
                // Department delete method
                break;
            case 12:
                // Faculty delete method
                break;
            case 13:
                // Student delete method
                crudOperations.deleteStudent();
                break;
            case 14:
                // Teacher delete method
                break;
            default:
                System.err.println("Command undefined. Word: 2");
        }
    }

    private void moveCommand () {
        // Student move method
    }

    private void updateCommand () {
        switch (commandCode[1]) {
            case 11:
                // Department update method
                break;
            case 12:
                // Faculty update method
                break;
            case 13:
                // Student update method
                crudOperations.updateStudent();
                break;
            case 14:
                // Teacher update method
                break;
            default:
                System.err.println("Command undefined. Word: 2");
        }
    }

/////////////////////////////////////////////////////////////////////
////               SECOND LEVEL OF ADMIN COMMAND                 ////
/////////////////////////////////////////////////////////////////////

    private void createCommand () {
        // Login create method
    }
}
