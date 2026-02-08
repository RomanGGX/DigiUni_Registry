package ua.university.service;

import ua.university.domain.*;
import ua.university.repository.StudentRepository;
import java.util.Scanner;

public class CRUDOperations {

    private final Scanner scanner = new Scanner(System.in);
    private final InputValidator inputValidator = new InputValidator();
    private final StudentRepository studentRepository;

    public CRUDOperations(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    ///////////////////////
    /// SHOW OPERATIONS ///
    ///////////////////////
    /*
    Displays universities list
     */
    public void showUniversities(University[] universities){
        if(universities == null || universities.length == 0) {
            System.out.println("Information about universities was not found");
            return;
        }
        System.out.println("---Universities List---");
        for (University university : universities){
            System.out.println(university.toString());
        }
    }
    /*
    Displays faculties list
     */
    public void showFaculties(Faculty[] faculties) {
        if(faculties == null || faculties.length == 0){
            System.out.println("Information about faculties was not found");
            return;
        }
        System.out.println("---Faculties list---");
        for (Faculty faculty : faculties){
            System.out.println(faculty.toString());
        }
    }

    /*
    Displays departments list
     */
    public void showDepartments(Department[] departments) {
        if(departments == null || departments.length == 0){
            System.out.println("Information about departments was not found");
            return;
        }
        System.out.println("---Departments list---");
        for (Department department : departments){
            System.out.println(department.toString());
        }
    }

    /*
     Displays persons list
     */
    public void showPersons(Person[] persons) {
        if(persons == null || persons.length == 0){
            System.out.println("Information about persons was not found");
            return;
        }
        System.out.println("---Persons list---");
        for (Person person: persons){
            System.out.println(person.toString());
        }
    }
    /*
     Displays teachers list
     */
    public void showTeachers(Teacher[] teachers) {
        if(teachers == null || teachers.length == 0){
            System.out.println("Information about teachers was not found");
            return;
        }
        System.out.println("---Teachers list---");
        for (Teacher teacher : teachers){
            System.out.println(teacher.toString());
        }
    }
    /*
     Displays students list
     */
    public void showStudents(Student[] students) {
        if(students == null || students.length == 0){
            System.out.println("Information about students was not found");
            return;
        }
        System.out.println("---Students list---");
        for (Student student : students){
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
        System.out.println("---Add New Student---");

        int id = studentRepository.getNextId();
        System.out.println("Auto-generated ID: " + id);

        String firstName = "";
        do {
            System.out.print("Enter first name: ");
            firstName = scanner.next().trim();
            if (inputValidator.checkWord(firstName).equals("-1")) {
                System.out.println("Invalid name. Please use only letters.");
            }
        } while (inputValidator.checkWord(firstName).equals("-1"));

        String middleName = "";
        do {
            System.out.print("Enter middle name: ");
            middleName = scanner.next().trim();
            if (inputValidator.checkWord(middleName).equals("-1")) {
                System.out.println("Invalid name. Please use only letters.");
            }
        } while (inputValidator.checkWord(middleName).equals("-1"));

        String lastName = "";
        do {
            System.out.print("Enter last name: ");
            lastName = scanner.next().trim();
            if (inputValidator.checkWord(lastName).equals("-1")) {
                System.out.println("Invalid name. Please use only letters.");
            }
        } while (inputValidator.checkWord(lastName).equals("-1"));

        System.out.print("Enter birth date (DD.MM.YYYY): ");
        String birthDate = scanner.next().trim();

        System.out.print("Enter email: ");
        String email = scanner.next().trim();

        System.out.print("Enter phone number (+380XXXXXXXXX): ");
        String phoneNumber = scanner.next().trim();

        String studentID = "";
        do {
            System.out.print("Enter student ID (6 characters, e.g., 4BI92H): ");
            studentID = scanner.next().trim();
            studentID = inputValidator.checkStudentID(studentID);
            if (studentID.equals("-1")) {
                System.out.println("Invalid student ID format. Example: '4BI92H'");
            }
        } while (studentID.equals("-1"));

        int course = 0;
        do {
            try {
                System.out.print("Enter course (1-6): ");
                course = Integer.parseInt(scanner.next().trim());
                if (course < 1 || course > 6) {
                    System.out.println("Course must be between 1 and 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } while (course < 1 || course > 6);

        String group = "";
        do {
            System.out.print("Enter group (e.g., 12A): ");
            group = scanner.next().trim();
            group = inputValidator.checkGroup(group);
            if (group.equals("-1")) {
                System.out.println("Invalid group format. Example: '12A'");
            }
        } while (group.equals("-1"));

        int yearEnroll = 0;
        do {
            try {
                System.out.print("Enter year of enrollment: ");
                yearEnroll = Integer.parseInt(scanner.next().trim());
                if (yearEnroll <= 0) {
                    System.out.println("Year must be a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } while (yearEnroll <= 0);

        System.out.print("Enter study form (бюджет/контракт): ");
        String studyForm = scanner.next().trim();

        System.out.print("Enter status (навчається/академвідпустка/відрахований) : ");
        String status = scanner.next().trim();

        Student newStudent = new Student(id, firstName, middleName, lastName, birthDate, email,
                phoneNumber, studentID, course, group, yearEnroll, studyForm, status);

        studentRepository.addStudent(newStudent);

        System.out.println("Student added successfully!");
    }

    /////////////////////////
    /// DELETE OPERATIONS ///
    /////////////////////////

    /**
     * Deletes a student from the repository by ID or full name.
     * Provides two search options and displays success or error message based on result.
     */
    public void deleteStudent() {
        System.out.println("---Delete Student---");

        System.out.println("\nChoose deletion method:");
        System.out.println("1. By ID (number)");
        System.out.println("2. By Full Name");
        System.out.print("Enter choice (1 or 2): ");

        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.next().trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input!");
            return;
        }

        boolean deleted = false;

        switch (choice) {
            case 1:
                // Deleting by ID
                System.out.print("Enter student ID (number): ");
                try {
                    int id = Integer.parseInt(scanner.next().trim());
                    deleted = this.studentRepository.deleteStudentById(id);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid ID format!");
                    return;
                }
                break;

            case 2:
                // Deleting by full name
                String[] names = { "first", "middle", "last" };
                String firstName = "";
                String middleName = "";
                String lastName = "";

                do {
                    System.out.print("Enter first name: ");
                    firstName = scanner.next().trim();
                    if (inputValidator.checkWord(firstName).equals("-1")) {
                        System.out.println("Invalid name. Please use only letters.");
                    }
                } while (inputValidator.checkWord(firstName).equals("-1"));

                do {
                    System.out.print("Enter middle name: ");
                    middleName = scanner.next().trim();
                    if (inputValidator.checkWord(middleName).equals("-1")) {
                        System.out.println("Invalid name. Please use only letters.");
                    }
                } while (inputValidator.checkWord(middleName).equals("-1"));

                do {
                    System.out.print("Enter last name: ");
                    lastName = scanner.next().trim();
                    if (inputValidator.checkWord(lastName).equals("-1")) {
                        System.out.println("Invalid name. Please use only letters.");
                    }
                } while (inputValidator.checkWord(lastName).equals("-1"));

                String[] fullName = {firstName, middleName, lastName};
                deleted = this.studentRepository.deleteStudentByFullName(fullName);
                break;

            default:
                System.err.println("Invalid choice!");
                return;
        }

        if (deleted) {
            System.out.println("Student deleted successfully!");
        }
        else {
            System.err.println("Student not found!");
        }
    }

    /////////////////////////
    /// UPDATE OPERATIONS ///
    /////////////////////////

    /**
     * Updates an existing student's information.
     * All the information about student (full name, group format, enrollment year, date of birth, email,
     * phone number, student ID, curse, study form, and status) can be changed.
     * Write 's' (same) to keep current value.
     */
    public void updateStudent() {
        System.out.println("---Update Student---");

        System.out.println("\nChoose search method:");
        System.out.println("1. By ID (number)");
        System.out.println("2. By Full Name");
        System.out.print("Enter choice (1 or 2): ");

        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.next().trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input!");
            return;
        }

        Student currentStudent = null;

        switch (choice) {
            case 1:
                // Search by ID
                System.out.print("Enter student ID (number): ");
                try {
                    int searchId = Integer.parseInt(scanner.next().trim());
                    Student[] allStudents = this.studentRepository.getStudents();
                    for (Student student : allStudents) {
                        if (student.getId() == searchId) {
                            currentStudent = student;
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid ID format!");
                    return;
                }
                break;

            case 2:
                // Search by full name
                String firstName = "";
                String middleName = "";
                String lastName = "";

                do {
                    System.out.print("Enter first name: ");
                    firstName = scanner.next().trim();
                    if (inputValidator.checkWord(firstName).equals("-1")) {
                        System.out.println("Invalid name. Please use only letters.");
                    }
                } while (inputValidator.checkWord(firstName).equals("-1"));

                do {
                    System.out.print("Enter middle name: ");
                    middleName = scanner.next().trim();
                    if (inputValidator.checkWord(middleName).equals("-1")) {
                        System.out.println("Invalid name. Please use only letters.");
                    }
                } while (inputValidator.checkWord(middleName).equals("-1"));

                do {
                    System.out.print("Enter last name: ");
                    lastName = scanner.next().trim();
                    if (inputValidator.checkWord(lastName).equals("-1")) {
                        System.out.println("Invalid name. Please use only letters.");
                    }
                } while (inputValidator.checkWord(lastName).equals("-1"));

                String[] fullName = {firstName, middleName, lastName};
                java.util.Optional<Student> studentOpt = this.studentRepository.FindByFullName(fullName);
                if (studentOpt.isPresent()) {
                    currentStudent = studentOpt.get();
                }
                break;

            default:
                System.err.println("Invalid choice!");
                return;
        }

        if (currentStudent == null) {
            System.err.println("\nStudent not found!");
            return;
        }

        System.out.println("\n--- Edit student information (type 's' to keep current value) ---");

        int id = currentStudent.getId();

        String newFirstName = "";
        do {
            System.out.print("Enter first name [" + currentStudent.getFirstName() + "]: ");
            newFirstName = scanner.next().trim();

            if (newFirstName.equalsIgnoreCase("s")) {
                newFirstName = currentStudent.getFirstName();
                break;
            }

            if (inputValidator.checkWord(newFirstName).equals("-1")) {
                System.out.println("Invalid name. Please use only letters or 's' to keep current.");
            }
        } while (inputValidator.checkWord(newFirstName).equals("-1"));

        String newMiddleName = "";
        do {
            System.out.print("Enter middle name [" + currentStudent.getMiddleName() + "]: ");
            newMiddleName = scanner.next().trim();

            if (newMiddleName.equalsIgnoreCase("s")) {
                newMiddleName = currentStudent.getMiddleName();
                break;
            }

            if (inputValidator.checkWord(newMiddleName).equals("-1")) {
                System.out.println("Invalid name. Please use only letters or 's' to keep current.");
            }
        } while (inputValidator.checkWord(newMiddleName).equals("-1"));

        String newLastName = "";
        do {
            System.out.print("Enter last name [" + currentStudent.getLastName() + "]: ");
            newLastName = scanner.next().trim();

            if (newLastName.equalsIgnoreCase("s")) {
                newLastName = currentStudent.getLastName();
                break;
            }

            if (inputValidator.checkWord(newLastName).equals("-1")) {
                System.out.println("Invalid name. Please use only letters or 's' to keep current.");
            }
        } while (inputValidator.checkWord(newLastName).equals("-1"));

        System.out.print("Enter birth date [" + currentStudent.getBirthDate() + "] (DD.MM.YYYY): ");
        String newBirthDate = scanner.next().trim();
        if (newBirthDate.equalsIgnoreCase("s")) {
            newBirthDate = currentStudent.getBirthDate();
        }

        System.out.print("Enter email [" + currentStudent.getEmail() + "]: ");
        String newEmail = scanner.next().trim();
        if (newEmail.equalsIgnoreCase("s")) {
            newEmail = currentStudent.getEmail();
        }

        System.out.print("Enter phone number [" + currentStudent.getPhoneNumber() + "] (+380XXXXXXXXX): ");
        String newPhoneNumber = scanner.next().trim();
        if (newPhoneNumber.equalsIgnoreCase("s")) {
            newPhoneNumber = currentStudent.getPhoneNumber();
        }

        String newStudentID = "";
        do {
            System.out.print("Enter student ID [" + currentStudent.getStudentId() + "] (6 characters, e.g., 4BI92H): ");
            newStudentID = scanner.next().trim();

            if (newStudentID.equalsIgnoreCase("s")) {
                newStudentID = currentStudent.getStudentId();
                break;
            }

            newStudentID = inputValidator.checkStudentID(newStudentID);
            if (newStudentID.equals("-1")) {
                System.out.println("Invalid student ID format. Example: '4BI92H' or 's' to keep current.");
            }
        } while (newStudentID.equals("-1"));

        int newCourse = 0;
        boolean validCourse = false;
        do {
            try {
                System.out.print("Enter course [" + currentStudent.getCourse() + "] (1-6): ");
                String courseInput = scanner.next().trim();

                if (courseInput.equalsIgnoreCase("s")) {
                    newCourse = currentStudent.getCourse();
                    validCourse = true;
                } else {
                    newCourse = Integer.parseInt(courseInput);
                    if (newCourse < 1 || newCourse > 6) {
                        System.out.println("Course must be between 1 and 6, or 's' to keep current.");
                    } else {
                        validCourse = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number or 's' to keep current.");
            }
        } while (!validCourse);

        String newGroup = "";
        do {
            System.out.print("Enter group [" + currentStudent.getGroup() + "] (e.g., 12A): ");
            newGroup = scanner.next().trim();

            if (newGroup.equalsIgnoreCase("s")) {
                newGroup = currentStudent.getGroup();
                break;
            }

            newGroup = inputValidator.checkGroup(newGroup);
            if (newGroup.equals("-1")) {
                System.out.println("Invalid group format. Example: '12A' or 's' to keep current.");
            }
        } while (newGroup.equals("-1"));

        int newYearEnroll = 0;
        boolean validYear = false;
        do {
            try {
                System.out.print("Enter year of enrollment [" + currentStudent.getYearEnroll() + "] : ");
                String yearInput = scanner.next().trim();

                if (yearInput.equalsIgnoreCase("s")) {
                    newYearEnroll = currentStudent.getYearEnroll();
                    validYear = true;
                } else {
                    newYearEnroll = Integer.parseInt(yearInput);
                    if (newYearEnroll < 1658 || newYearEnroll > 2026) {
                        System.out.println("Year must be between 1658 and 2026, or 's' to keep current.");
                    } else {
                        validYear = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid year or 's' to keep current.");
            }
        } while (!validYear);

        System.out.print("Enter study form [" + currentStudent.getStudyForm() + "] (бюджет/контракт): ");
        String newStudyForm = scanner.next().trim();
        if (newStudyForm.equalsIgnoreCase("s")) {
            newStudyForm = currentStudent.getStudyForm();
        }

        System.out.print("Enter status [" + currentStudent.getStatus() + "]: (навчається/академвідпустка/відрахований) ");
        String newStatus = scanner.next().trim();
        if (newStatus.equalsIgnoreCase("s")) {
            newStatus = currentStudent.getStatus();
        }

        Student updatedStudent = new Student(id, newFirstName, newMiddleName, newLastName, newBirthDate, newEmail,
                newPhoneNumber, newStudentID, newCourse, newGroup, newYearEnroll, newStudyForm, newStatus
        );

        boolean updated = this.studentRepository.updateStudent(id, updatedStudent);

        if (updated) {
            System.out.println("\nStudent updated successfully!");
        } else {
            System.err.println("\nFailed to update student!");
        }
    }

}