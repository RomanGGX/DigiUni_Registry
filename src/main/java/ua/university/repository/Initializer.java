package ua.university.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.university.domain.*;
import ua.university.exceptions.InitializationFailedException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Initializer {
    private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

    /** Initializes all repositories */
    public static void initializeAll(StudentRepository studentRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository, UserRepository userRepository, Path dataPath) throws IOException, InitializationFailedException {
        ObjectMapper mapper = new ObjectMapper();

        Thread facultiesThread = new Thread(() -> {
            try {
                initializeFaculties(mapper, dataPath, facultyRepository);
            } catch (IOException e) {
                logger.error("Failed to initialize faculties");
                throw new InitializationFailedException("Failed to initialize faculties", e);
            }
        }, "Faculties load");
        Thread departmentsThread = new Thread(() -> {
            try {
                initializeDepartments(mapper, dataPath, departmentRepository);
            } catch (IOException e) {
                logger.error("Failed to initialize departments");
                throw new InitializationFailedException("Failed to initialize departments", e);
            }
        }, "Departments load");
        Thread studentsThread = new Thread(() -> {
            try {
                initializeStudents(mapper, dataPath, studentRepository);
            } catch (IOException e) {
                logger.error("Failed to initialize students");
                throw new InitializationFailedException("Failed to initialize students", e);
            }
        }, "Students load");
        Thread teachersThread = new Thread(() -> {
            try {
                initializeTeachers(mapper, dataPath, teacherRepository);
            } catch (IOException e) {
                logger.error("Failed to initialize teachers");
                throw new InitializationFailedException("Failed to initialize teachers", e);
            }
        }, "Teachers load");

        facultiesThread.start();
        departmentsThread.start();
        studentsThread.start();
        teachersThread.start();

        try {
            facultiesThread.join();
            departmentsThread.join();
            studentsThread.join();
            teachersThread.join();
        } catch (InterruptedException e) {
            logger.error("Threads interrupted in initialization");
            throw new IOException("Threads interrupted in initialization", e);
        }

        connectRepositories(studentRepository, facultyRepository, departmentRepository, teacherRepository);
    }

    /** Initializes faculties */
    private static void initializeFaculties(ObjectMapper mapper, Path dataPath, FacultyRepository facultyRepository) throws IOException {
        List<Faculty> faculties = mapper.readValue(
                dataPath.resolve("Faculties.json").toFile(),
                new TypeReference<>() {}
        );
        for (Faculty f : faculties) {
            facultyRepository.add(f);
        }
    }

    /** Initializes faculties */
    private static void initializeDepartments(ObjectMapper mapper, Path dataPath, DepartmentRepository departmentRepository) throws IOException {
        List<Department> departments = mapper.readValue(
                dataPath.resolve("Departments.json").toFile(),
                new TypeReference<>() {}
        );
        for (Department d : departments) {
            departmentRepository.add(d);
        }
    }

    /** Initializes faculties */
    private static void initializeStudents(ObjectMapper mapper, Path dataPath, StudentRepository studentRepository) throws IOException {
        List<Student> students = mapper.readValue(
                dataPath.resolve("Students.json").toFile(),
                new TypeReference<>() {}
        );
        for (Student s : students) {
            studentRepository.add(s);
        }
    }

    /** Initializes faculties */
    private static void initializeTeachers(ObjectMapper mapper, Path dataPath, TeacherRepository teacherRepository) throws IOException {
        List<Teacher> teachers = mapper.readValue(
                dataPath.resolve("Teachers.json").toFile(),
                new TypeReference<>() {}
        );
        for (Teacher t : teachers) {
            teacherRepository.add(t);
        }
    }

    /** Connects repositories by links */
    public static void connectRepositories(StudentRepository studentRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository) {
        Thread connectStudentsThread = new Thread(() -> connectStudents(studentRepository, departmentRepository), "Students connect");
        Thread connectFacultiesThread = new Thread(() -> connectFaculties(facultyRepository, teacherRepository), "Faculties connect");
        Thread connectDepartmentsThread = new Thread(() -> connectDepartments(departmentRepository, facultyRepository, teacherRepository), "Departments connect");
        Thread connectTeachersThread = new Thread(() -> connectTeachers(teacherRepository, departmentRepository), "Teachers connect");

        connectStudentsThread.start();
        connectFacultiesThread.start();
        connectDepartmentsThread.start();
        connectTeachersThread.start();
    }

    /** Connects student repository */
    private static void connectStudents(StudentRepository studentRepository, DepartmentRepository departmentRepository) {
        for (Student s : studentRepository.findAll()) {
            if (s.getDepartment() != null) {
                s.setDepartment(departmentRepository.findById(s.getDepartment().getCode()).orElse(null));
                studentRepository.update(s.getId(), s);
            }
        }
    }

    /** Connects faculty repository */
    private static void connectFaculties(FacultyRepository facultyRepository, TeacherRepository teacherRepository) {
        for (Faculty f : facultyRepository.findAll()) {
            if (f.getDean() != null) {
                f.setDean(teacherRepository.findById(f.getDean().getId()).orElse(null));
                facultyRepository.update(f.getCode(), f);
            }
        }
    }

    /** Connects department repository */
    private static void connectDepartments(DepartmentRepository departmentRepository, FacultyRepository facultyRepository, TeacherRepository teacherRepository) {
        for (Department d : departmentRepository.findAll()) {
            if (d.getFaculty() != null) {
                d.setFaculty(facultyRepository.findById(d.getFaculty().getCode()).orElse(null));
            }
            if (d.getHead() != null) {
                d.setHead(teacherRepository.findById(d.getHead().getId()).orElse(null));
            }
            departmentRepository.update(d.getCode(), d);
        }
    }

    private static void connectTeachers(TeacherRepository teacherRepository, DepartmentRepository departmentRepository) {
        for (Teacher t : teacherRepository.findAll()) {
            if (t.getDepartment() != null) {
                t.setDepartment(departmentRepository.findById(t.getDepartment().getCode()).orElse(null));
                teacherRepository.update(t.getId(), t);
            }
        }
    }
}