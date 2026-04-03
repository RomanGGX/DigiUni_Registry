package ua.university.repository;

import ua.university.domain.*;
import ua.university.service.IOOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Initializer {

    /** Initializes all repositories */
    public static void initializeAll(StudentRepository studentRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository, UserRepository userRepository) throws IOException {
        IOOperations ioOperations = new IOOperations(Path.of("data"), Path.of("data"));

        initializeFaculties(ioOperations, facultyRepository);
        initializeDepartments(ioOperations, departmentRepository);
        initializeStudents(ioOperations, studentRepository);
        initializeTeachers(ioOperations, teacherRepository);

        setDeansAndHeads(ioOperations, facultyRepository, departmentRepository, teacherRepository);
        setFacultyForDepartment(ioOperations, departmentRepository, facultyRepository);
        setDepartmentsForPersons(ioOperations, studentRepository, teacherRepository, departmentRepository);
    }

    /** Initializes faculties */
    private static void initializeFaculties(IOOperations ioOperations, FacultyRepository facultyRepository) throws IOException {
        Path path = ioOperations.getRunning();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean facultiesSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(".Faculties")) facultiesSegment = true;
                else if (facultiesSegment) {
                    if (line.startsWith("#")) {
                        String[] segmentedLine = line.substring(1).split("#");

                        int code = Integer.parseInt(segmentedLine[0]);
                        String name = segmentedLine[1];
                        String shortName = segmentedLine[2];
                        String contacts = segmentedLine[4];

                        facultyRepository.add(
                                new Faculty(code, name, shortName, null, contacts)
                        );
                    } else break;
                }
            }
        }
    }

    /** Initializes departments */
    private static void initializeDepartments(IOOperations ioOperations, DepartmentRepository departmentRepository) throws IOException {
        Path path = ioOperations.getRunning();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean departmentsSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(".Departments")) departmentsSegment = true;
                else if (departmentsSegment) {
                    if (line.startsWith("#")) {
                        String[] segmentedLine = line.substring(1).split("#");

                        int code = Integer.parseInt(segmentedLine[0]);
                        String name = segmentedLine[1];
                        String cabinet = segmentedLine[4];

                        departmentRepository.add(
                                new Department(code, name, null, null, cabinet)
                        );
                    } else break;
                }
            }
        }
    }

    /** Initializes students */
    private static void initializeStudents(IOOperations ioOperations, StudentRepository studentRepository) throws IOException {
        Path path = ioOperations.getRunning();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean studentsSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(".Students")) studentsSegment = true;
                else if (studentsSegment) {
                    if (line.startsWith("#")) {
                        String[] segmentedLine = line.substring(1).split("#");

                        int id = Integer.parseInt(segmentedLine[0]);
                        String firstName = segmentedLine[1];
                        String middleName = segmentedLine[2];
                        String lastName = segmentedLine[3];
                        String birthDate = segmentedLine[4];
                        String email = segmentedLine[5];
                        String phoneNumber = segmentedLine[6];
                        String studentId = segmentedLine[7];
                        int course = Integer.parseInt(segmentedLine[8]);
                        String group = segmentedLine[9];
                        int yearEnroll = Integer.parseInt(segmentedLine[10]);
                        String studyForm = segmentedLine[11];
                        String status = segmentedLine[12];

                        studentRepository.add(
                                new Student(id, firstName, middleName, lastName, birthDate,
                                        email, phoneNumber, studentId, course, group,
                                        yearEnroll, studyForm, status, null)
                        );
                    } else break;
                }
            }
        }
    }

    /** Initializes teachers */
    private static void initializeTeachers(IOOperations ioOperations, TeacherRepository teacherRepository) throws IOException {
        Path path = ioOperations.getRunning();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean teachersSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(".Teachers")) teachersSegment = true;
                else if (teachersSegment) {
                    if (line.startsWith("#")) {
                        String[] segmentedLine = line.substring(1).split("#");

                        int id = Integer.parseInt(segmentedLine[0]);
                        String firstName = segmentedLine[1];
                        String middleName = segmentedLine[2];
                        String lastName = segmentedLine[3];
                        String birthDate = segmentedLine[4];
                        String email = segmentedLine[5];
                        String phoneNumber = segmentedLine[6];
                        String position = segmentedLine[7];
                        String academicDegree = segmentedLine[9];
                        String academicTitle = segmentedLine[10];
                        String employmentDate = segmentedLine[11];
                        double workload = Double.parseDouble(segmentedLine[12]);

                        teacherRepository.add(
                                new Teacher(id, firstName, middleName, lastName, birthDate,
                                        email, phoneNumber, position, null,
                                        academicDegree, academicTitle, employmentDate,
                                        workload)
                        );
                    } else break;
                }
            }
        }
    }

    /** Sets deans and heads for faculties and departments */
    private static void setDeansAndHeads(IOOperations ioOperations, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository) throws IOException {
        for (Faculty f : facultyRepository.findAll()) {
            Teacher teacher = teacherRepository.findByEmail(getDeanEmailForFaculty(ioOperations, f)).orElse(null);
            try {
                facultyRepository.findById(f.getCode()).orElse(null).setDean(teacher);
            } catch (NullPointerException ex) {
                // Add logger for faculty not found. Level error
            }
            facultyRepository.update(f.getCode(), f);
        }

        for (Department d : departmentRepository.findAll()) {
            Teacher teacher = teacherRepository.findByEmail(getHeadEmailForDepartment(ioOperations, d)).orElse(null);
            try {
                departmentRepository.findById(d.getCode()).orElse(null).setHead(teacher);
            } catch (NullPointerException ex) {
                // Add logger for department not found. Level error
            }
            departmentRepository.update(d.getCode(), d);
        }
    }

    /** Sets faculties for departments */
    private static void setFacultyForDepartment(IOOperations ioOperations, DepartmentRepository departmentRepository, FacultyRepository facultyRepository) throws IOException {
        for (Department d : departmentRepository.findAll()) {
            Faculty faculty = facultyRepository.findByShortName(getFacultyShortNameForDepartment(ioOperations, d)).orElse(null);
            try {
                departmentRepository.findById(d.getCode()).orElse(null).setFaculty(faculty);
            } catch (NullPointerException ex) {
                // Add logger for department not found. Level error
            }
            departmentRepository.update(d.getCode(), d);
        }
    }

    /** Sets departments for students and teachers */
    private static void setDepartmentsForPersons(IOOperations ioOperations, StudentRepository studentRepository, TeacherRepository teacherRepository, DepartmentRepository departmentRepository) throws IOException {
        for (Student s : studentRepository.findAll()) {
            Department department = departmentRepository.findByCabinet(getDepartmentCabinetForStudent(ioOperations, s)).orElse(null);
            try {
                studentRepository.findById(s.getId()).orElse(null).setDepartment(department);
            } catch (NullPointerException ex) {
                // Add logger for student not found. Level error
            }
            studentRepository.update(s.getId(), s);
        }

        for (Teacher t : teacherRepository.findAll()) {
            Department department = departmentRepository.findByCabinet(getDepartmentCabinetForTeacher(ioOperations, t)).orElse(null);
            try {
                teacherRepository.findById(t.getId()).orElse(null).setDepartment(department);
            } catch (NullPointerException ex) {
                // Add logger for teacher not found. Level error
            }
            teacherRepository.update(t.getId(), t);
        }
    }

    /** Gets dean email from file for faculty */
    private static String getDeanEmailForFaculty(IOOperations ioOperations, Faculty faculty) throws IOException {
        Path path = ioOperations.getRunning();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean facultiesSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(".Faculties")) facultiesSegment = true;
                else if (facultiesSegment) {
                    if (line.startsWith("#")) {
                        String[] segmentedLine = line.substring(1).split("#");

                        if (Integer.parseInt(segmentedLine[0]) - faculty.getCode() == 0) return segmentedLine[3];
                    } else break;
                }
            }
        }

        return null;
    }

    /** Gets head email from file for department */
    private static String getHeadEmailForDepartment(IOOperations ioOperations, Department department) throws IOException {
        Path path = ioOperations.getRunning();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean departmentsSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(".Departments")) departmentsSegment = true;
                else if (departmentsSegment) {
                    if (line.startsWith("#")) {
                        String[] segmentedLine = line.substring(1).split("#");

                        if (Integer.parseInt(segmentedLine[0]) - department.getCode() == 0) return segmentedLine[3];
                    } else break;
                }
            }
        }

        return null;
    }

    /** gets faculty short name for department */
    private static String getFacultyShortNameForDepartment(IOOperations ioOperations, Department department) throws IOException {
        Path path = ioOperations.getRunning();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean departnemtsSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(".Departments")) departnemtsSegment = true;
                else if (departnemtsSegment) {
                    if (line.startsWith("#")) {
                        String[] segmentedLine = line.substring(1).split("#");

                        if (Integer.parseInt(segmentedLine[0]) - department.getCode() == 0) return segmentedLine[2];
                    } else break;
                }
            }
        }
        
        return null;
    }

    /** Gets department cabinet for student */
    private static String getDepartmentCabinetForStudent(IOOperations ioOperations, Student student) throws IOException {
        Path path = ioOperations.getRunning();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean studentsSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(".Students")) studentsSegment = true;
                else if (studentsSegment) {
                    if (line.startsWith("#")) {
                        String[] segmentedLine = line.substring(1).split("#");

                        if (Integer.parseInt(segmentedLine[0]) - student.getId() == 0) return segmentedLine[13];
                    }
                }
            }
        }

        return null;
    }

    /** Gets department cabinet for teacher */
    private static String getDepartmentCabinetForTeacher(IOOperations ioOperations, Teacher teacher) throws  IOException {
        Path path = ioOperations.getRunning();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean teachersSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(".Teachers")) teachersSegment = true;
                else if (teachersSegment) {
                    if (line.startsWith("#")) {
                        String[] segmentedLine = line.substring(1).split("#");

                        if (Integer.parseInt(segmentedLine[0]) - teacher.getId() == 0) return segmentedLine[8];
                    }
                }
            }
        }

        return null;
    }
}