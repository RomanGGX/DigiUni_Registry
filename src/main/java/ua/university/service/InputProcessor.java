package ua.university.service;

import ua.university.repository.StudentRepository;

public class InputProcessor {

    /** Adds find operations to the class */
    private final FindOperations findOperations = new FindOperations();

    private CRUDOperations crudOperations;
    private StudentRepository studentRepository;

    public InputProcessor () {
        this.crudOperations = new CRUDOperations();
        this.studentRepository = new StudentRepository();
    }

    /**
     * Defines the method to use for a command
     * @param commandCode int array code of a command
     * @param accessLevel int access level
     */
    public void defineProcess (int[] commandCode, int accessLevel) {
        switch (accessLevel) {
            case 0:
                defineUserCommand(commandCode);
                break;
            case 1:
                defineManagerCommand(commandCode);
                break;
            case 2:
                defineAdminCommand(commandCode);
                break;
        }
    }

/////////////////////////////////////////////////////////////////////
////                    FIRST LEVEL OF COMMAND                   ////
/////////////////////////////////////////////////////////////////////

    private void defineUserCommand (int[] commandCode) {
        switch (commandCode[0]) {
            case 1:
                showCommand(commandCode);
                break;
            case 2:
                loginCommand(commandCode);
                break;
            case 3:
                exitCommand(commandCode);
                break;
            case 4:
                endCommand(commandCode);
                break;
            case 5:
                findCommand(commandCode);
                break;
            default:
                System.err.println("Command undefined. Word: 1");
                break;
        }
    }

    private void defineManagerCommand (int[] commandCode) {
        switch (commandCode[0]) {
            case 1:
                showCommand(commandCode);
                break;
            case 2:
                loginCommand(commandCode);
                break;
            case 3:
                exitCommand(commandCode);
                break;
            case 4:
                endCommand(commandCode);
                break;
            case 5:
                findCommand(commandCode);
            case 6:
                addCommand(commandCode);
                break;
            case 7:
                deleteCommand(commandCode);
                break;
            case 8:
                moveCommand(commandCode);
                break;
            case 9:
                updateCommand(commandCode);
                break;
            default:
                System.err.println("Command undefined. Word: 1");
                break;
        }
    }

    private void defineAdminCommand (int[] commandCode) {
        switch (commandCode[0]) {
            case 1:
                showCommand(commandCode);
                break;
            case 2:
                loginCommand(commandCode);
                break;
            case 3:
                exitCommand(commandCode);
                break;
            case 4:
                endCommand(commandCode);
                break;
            case 5:
                findCommand(commandCode);
            case 6:
                addCommand(commandCode);
                break;
            case 7:
                deleteCommand(commandCode);
                break;
            case 8:
                moveCommand(commandCode);
                break;
            case 9:
                updateCommand(commandCode);
                break;
            case 10:
                createCommand(commandCode);
                break;
            default:
                System.err.println("Command undefined. Word: 1");
                break;
        }
    }

/////////////////////////////////////////////////////////////////////
////               SECOND LEVEL OF USER COMMAND                  ////
/////////////////////////////////////////////////////////////////////

    private void showCommand (int[] commandCode) {
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

    private void loginCommand (int[] commandCode) {
        // Login method (unused for now)
    }

    private void exitCommand (int[] commandCode) {
        // Exit from the account method (unused for now)
    }

    private void endCommand (int[] commandCode) {
        // Program end method
    }

    private void findCommand (int[] commandCode) {
        if (commandCode[1] == 16) findByCommand(commandCode);
        else System.err.println("Command undefined. Word: 2");
    }

/////////////////////////////////////////////////////////////////////
////               THIRD LEVEL OF USER COMMAND                   ////
/////////////////////////////////////////////////////////////////////

    private void findByCommand (int[] commandCode) {
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

    private void addCommand (int[] commandCode) {
        switch (commandCode[1]) {
            case 11:
                // Department add method
                break;
            case 12:
                // Faculty add method
                break;
            case 13:
                // Student add method
                break;
            case 14:
                // Teacher add method
                break;
            default:
                System.err.println("Command undefined. Word: 2");
        }
    }

    private void deleteCommand (int[] commandCode) {
        switch (commandCode[1]) {
            case 11:
                // Department delete method
                break;
            case 12:
                // Faculty delete method
                break;
            case 13:
                // Student delete method
                break;
            case 14:
                // Teacher delete method
                break;
            default:
                System.err.println("Command undefined. Word: 2");
        }
    }

    private void moveCommand (int[] commandCode) {
        // Student move method
    }

    private void updateCommand (int[] commandCode) {
        // Data update method
    }

/////////////////////////////////////////////////////////////////////
////               SECOND LEVEL OF ADMIN COMMAND                 ////
/////////////////////////////////////////////////////////////////////

    private void createCommand (int[] commandCode) {
        // Login create method
    }
}
