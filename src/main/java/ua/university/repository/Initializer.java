package ua.university.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.domain.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Initializer {

    /** Initializes all repositories */
    public static void initializeAll(StudentRepository studentRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository, UserRepository userRepository, Path dataPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        initializeFaculties(mapper, dataPath, facultyRepository);
        initializeDepartments(mapper, dataPath, departmentRepository);
        initializeStudents(mapper, dataPath, studentRepository);
        initializeTeachers(mapper, dataPath, teacherRepository);
    }

    /** Initializes faculties */
    private static void initializeFaculties(ObjectMapper mapper, Path dataPath, FacultyRepository facultyRepository) throws IOException {
        List<Faculty> faculties = mapper.readValue(
                dataPath.resolve("stable").resolve("Faculties.json").toFile(),
                new TypeReference<>() {}
        );
        for (Faculty f : faculties) {
            facultyRepository.add(f);
        }
    }

    /** Initializes faculties */
    private static void initializeDepartments(ObjectMapper mapper, Path dataPath, DepartmentRepository departmentRepository) throws IOException {
        List<Department> departments = mapper.readValue(
                dataPath.resolve("stable").resolve("Departments.json").toFile(),
                new TypeReference<>() {}
        );
        for (Department d : departments) {
            departmentRepository.add(d);
        }
    }

    /** Initializes faculties */
    private static void initializeStudents(ObjectMapper mapper, Path dataPath, StudentRepository studentRepository) throws IOException {
        List<Student> students = mapper.readValue(
                dataPath.resolve("stable").resolve("Students.json").toFile(),
                new TypeReference<>() {}
        );
        for (Student s : students) {
            studentRepository.add(s);
        }
    }

    /** Initializes faculties */
    private static void initializeTeachers(ObjectMapper mapper, Path dataPath, TeacherRepository teacherRepository) throws IOException {
        List<Teacher> teachers = mapper.readValue(
                dataPath.resolve("stable").resolve("Teachers.json").toFile(),
                new TypeReference<>() {}
        );
        for (Teacher t : teachers) {
            teacherRepository.add(t);
        }
    }
}