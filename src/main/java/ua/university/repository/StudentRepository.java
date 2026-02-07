package ua.university.repository;

import ua.university.domain.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository {
    private Student[] students;

    public StudentRepository() {
        ExampleStudents();
    }

    public Student[] getStudents() {
        return students.clone();
    }

    private void ExampleStudents() {
        students = new Student[2];

        students[0] = new Student(1, "Іван", "Іванович", "Шевченко", "02.03.2007",
                "ivan@gmail.com", "+380534386432", "3F34AB", 1, "42B",
                2025, "бюджет", "навчається");

        students[1] = new Student(2, "Марія", "Ігорівна", "Мельник", "18.11.2006",
                "maria@gmail.com", "+380839301980", "1K91AB", 3, "10B",
                2023, "контракт", "навчається");
    }

    /**
     * Checks if the student with the same full name is presented
     * @param names String array of first, middle and last names
     * @return Optional student if it finds, and optional empty if not
     */
    public Optional<Student> FindByFullName (String[] names) {
        for (Student student : students) {
            if (student.getFirstName().equalsIgnoreCase(names[0])) {
                if (student.getMiddleName().equalsIgnoreCase(names[1])) {
                    if (student.getLastName().equalsIgnoreCase(names[2])) return Optional.of(student);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Makes a list of students of the same course
     * @param course int course
     * @return List of students by course
     */
    public List<Student> FindByCourse (int course) {
        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (course == student.getCourse()) result.add(student);
        }
        return result;
    }

    /**
     * Makes a list of students of the same group
     * @param group String group
     * @return List of students by group
     */
    public List<Student> FindByGroup (String group) {
        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (group.equalsIgnoreCase(student.getGroup())) result.add(student);
        }
        return result;
    }

    /**
     * Checks if the student with the same student ID is presented
     * @param studentID String student ID
     * @return Optional student if it finds, and optional empty if not
     */
    public Optional<Student> FindByStudentID (String studentID) {
        for (Student student : students) {
            if (studentID.equalsIgnoreCase(student.getStudentId())) return Optional.of(student);
        }
        return Optional.empty();
    }

    /**
     * Checks if the student with the same email is presented
     * @param email String email
     * @return Optional student if it finds, and optional empty if not
     */
    public Optional<Student> FindByEmail (String email) {
        for (Student student : students) {
            if (email.equalsIgnoreCase(student.getEmail())) return Optional.of(student);
        }
        return Optional.empty();
    }

    /**
     * Adds a new student to the repository.
     * Creates a new array with increased size and appends the student to the end.
     * @param student the student to be added
     */
    public void addStudent(Student student) {
        Student[] newArray = new Student[students.length + 1];

        for (int i = 0; i < students.length; i++) {
            newArray[i] = students[i];
        }

        newArray[students.length] = student;

        students = newArray;
    }

    /**
     * Deletes a student from the repository by their ID.
     * @param id the unique identifier of the student
     * @return true if student was found and deleted, false otherwise
     */
    public boolean deleteStudentById(int id) {
        boolean found = false;
        for (Student student : students) {
            if (student.getId() == id) {
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        Student[] newArray = new Student[students.length - 1];

        int newIndex = 0;
        for (int i = 0; i < students.length; i++) {
            if (students[i].getId() != id) {
                newArray[newIndex] = students[i];
                newIndex++;
            }
        }

        students = newArray;
        return true;
    }

    /**
     * Deletes a student from the repository by their full name.
     * @param names String array of first, middle and last names
     * @return true if student was found and deleted, false otherwise
     */
    public boolean deleteStudentByFullName(String[] names) {
        Optional<Student> studentOpt = this.FindByFullName(names);

        if (studentOpt.isPresent()) {
            return deleteStudentById(studentOpt.get().getId());
        }
        return false;
    }

    /**
     * Updates an existing student's information in the repository.
     * @param id the unique identifier of the student to update
     * @param updatedStudent the student object with new information
     * @return true if student was found and updated, false otherwise
     */
    public boolean updateStudent(int id, Student updatedStudent) {
        for (int i = 0; i < students.length; i++) {
            if (students[i].getId() == id) {
                students[i] = updatedStudent;
                return true;
            }
        }
        return false;
    }

    /**
     * Generates the next available ID for a new student.
     * Finds the maximum ID in the repository and returns it incremented by 1.
     * @return the next available student ID
     */
    public int getNextId() {
        int maxId = 0;
        for (Student student : students) {
            if (student.getId() > maxId) {
                maxId = student.getId();
            }
        }
        return maxId + 1;
    }
}