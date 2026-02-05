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
     * @return Optional student if finds, and optional empty if not
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

    public List<Student> FindByCourse (int course) {
        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (course == student.getCourse()) result.add(student);
        }
        return result;
    }

    public List<Student> FindByGroup (String group) {
        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (group.equalsIgnoreCase(student.getGroup())) result.add(student);
        }
        return result;
    }

    public Optional<Student> FindByStudentID (String studentID) {
        for (Student student : students) {
            if (studentID.equalsIgnoreCase(student.getStudentId())) return Optional.of(student);
        }
        return Optional.empty();
    }

    public Optional<Student> FindByEmail (String email) {
        for (Student student : students) {
            if (email.equalsIgnoreCase(student.getEmail())) return Optional.of(student);
        }
        return Optional.empty();
    }
}
