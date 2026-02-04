package ua.university.repository;

import ua.university.domain.Student;

public class StudentRepository {
    private Student[] students;

    public StudentRepository() {
        ExampleStudents();
    }

    public Student[] getStudents() {
        return students;
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
}
