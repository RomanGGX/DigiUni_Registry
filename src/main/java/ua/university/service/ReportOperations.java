package ua.university.service;

import ua.university.domain.*;
import ua.university.repository.*;

import java.util.*;

public class ReportOperations {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final Scanner scanner = new Scanner(System.in);

    public ReportOperations(StudentRepository studentRepository,
                            TeacherRepository teacherRepository,
                            FacultyRepository facultyRepository,
                            DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;;
    }

    public void showAllStudentsByCourse() {
        System.out.println("---Всі студенти, впорядковані за курсами---");

        List<Student> students = new ArrayList<>(studentRepository.findAll());
        if (students.isEmpty()) {
            System.err.println("Немає студентів!");
            return;
        }

        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                int courseCompare = Integer.compare(s1.getCourse(), s2.getCourse());
                if (courseCompare != 0) {
                    return courseCompare;
                }
                return s1.getLastName().compareTo(s2.getLastName());
            }
        });

        System.out.println("\n=== Студенти за курсами ===");
        int currentCourse = -1;
        for (Student student : students) {
            if (student.getCourse() != currentCourse) {
                currentCourse = student.getCourse();
                System.out.println("\n--- Курс " + currentCourse + " ---");
            }
            String facultyName = student.getFaculty() != null ? student.getFaculty().getShortName() : "немає";
            String deptName = student.getDepartment() != null ? student.getDepartment().getName() : "немає";
            System.out.println("[ID: " + student.getId() + "] " + student.getFullName() +
                    ", Група: " + student.getGroup() +
                    ", Факультет: " + facultyName +
                    ", Кафедра: " + deptName);
        }
        System.out.println("\nВсього студентів: " + students.size());
    }

    public void showStudentsByFacultyAlphabetically() {
        System.out.println("---Студенти факультету за алфавітом---");

        List<Faculty> faculties = facultyRepository.findAll();
        if (faculties.isEmpty()) {
            System.err.println("Немає факультетів!");
            return;
        }

        Faculty selectedFaculty = null;
        boolean facultySelected = false;

        while (!facultySelected) {
            System.out.println("\n=== Оберіть факультет ===");
            for (int i = 0; i < faculties.size(); i++) {
                System.out.println((i + 1) + ". " + faculties.get(i).getShortName() + " - " + faculties.get(i).getName());
            }

            System.out.print("Номер факультету: ");
            String facultyInput = scanner.nextLine().trim();

            if (facultyInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім!");
                continue;
            }

            try {
                int facultyChoice = Integer.parseInt(facultyInput);
                if (facultyChoice >= 1 && facultyChoice <= faculties.size()) {
                    selectedFaculty = faculties.get(facultyChoice - 1);
                    facultySelected = true;
                } else {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + faculties.size());
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        List<Student> students = new ArrayList<>(studentRepository.findByFaculty(selectedFaculty));
        if (students.isEmpty()) {
            System.err.println("На факультеті " + selectedFaculty.getShortName() + " немає студентів!");
            return;
        }

        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                int lastNameCompare = s1.getLastName().compareTo(s2.getLastName());
                if (lastNameCompare != 0) {
                    return lastNameCompare;
                }
                int firstNameCompare = s1.getFirstName().compareTo(s2.getFirstName());
                if (firstNameCompare != 0) {
                    return firstNameCompare;
                }
                return s1.getMiddleName().compareTo(s2.getMiddleName());
            }
        });

        System.out.println("\n=== Студенти факультету " + selectedFaculty.getShortName() + " (за алфавітом) ===");
        for (Student student : students) {
            String deptName = student.getDepartment() != null ? student.getDepartment().getName() : "немає";
            System.out.println("[ID: " + student.getId() + "] " + student.getFullName() +
                    ", Курс: " + student.getCourse() +
                    ", Група: " + student.getGroup() +
                    ", Кафедра: " + deptName);
        }
        System.out.println("\nВсього студентів на факультеті: " + students.size());
    }

    /**
     * Displays all teachers of a faculty, sorted alphabetically
     */
    public void showTeachersByFacultyAlphabetically() {
        System.out.println("---Викладачі факультету за алфавітом---");

        List<Faculty> faculties = facultyRepository.findAll();
        if (faculties.isEmpty()) {
            System.err.println("Немає факультетів!");
            return;
        }

        Faculty selectedFaculty = null;
        boolean facultySelected = false;

        while (!facultySelected) {
            System.out.println("\n=== Оберіть факультет ===");
            for (int i = 0; i < faculties.size(); i++) {
                System.out.println((i + 1) + ". " + faculties.get(i).getShortName() + " - " + faculties.get(i).getName());
            }

            System.out.print("Номер факультету: ");
            String facultyInput = scanner.nextLine().trim();

            if (facultyInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім!");
                continue;
            }

            try {
                int facultyChoice = Integer.parseInt(facultyInput);
                if (facultyChoice >= 1 && facultyChoice <= faculties.size()) {
                    selectedFaculty = faculties.get(facultyChoice - 1);
                    facultySelected = true;
                } else {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + faculties.size());
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        List<Teacher> teachers = teacherRepository.findByFaculty(selectedFaculty);
        if (teachers.isEmpty()) {
            System.err.println("На факультеті " + selectedFaculty.getShortName() + " немає викладачів!");
            return;
        }

        teachers.sort(new Comparator<Teacher>() {
            @Override
            public int compare(Teacher t1, Teacher t2) {
                int lastNameCompare = t1.getLastName().compareTo(t2.getLastName());
                if (lastNameCompare != 0) {
                    return lastNameCompare;
                }
                int firstNameCompare = t1.getFirstName().compareTo(t2.getFirstName());
                if (firstNameCompare != 0) {
                    return firstNameCompare;
                }
                return t1.getMiddleName().compareTo(t2.getMiddleName());
            }
        });

        System.out.println("\n=== Викладачі факультету " + selectedFaculty.getShortName() + " (за алфавітом) ===");
        for (Teacher teacher : teachers) {
            String deptName = teacher.getDepartment() != null ? teacher.getDepartment().getName() : "немає";
            System.out.println("[ID: " + teacher.getId() + "] " + teacher.getFullName() +
                    ", Посада: " + teacher.getPosition() +
                    ", Кафедра: " + deptName);
        }
        System.out.println("\nВсього викладачів на факультеті: " + teachers.size());
    }

    /**
     * Displays all students of a department, sorted by course
     */
    public void showStudentsByDepartmentByCourse() {
        System.out.println("---Студенти кафедри за курсами---");

        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            System.err.println("Немає кафедр!");
            return;
        }

        Department selectedDepartment = null;
        boolean departmentSelected = false;

        while (!departmentSelected) {
            System.out.println("\n=== Оберіть кафедру ===");
            for (int i = 0; i < departments.size(); i++) {
                String facultyName = departments.get(i).getFaculty() != null ?
                        departments.get(i).getFaculty().getShortName() : "немає";
                System.out.println((i + 1) + ". " + departments.get(i).getName() + " (" + facultyName + ")");
            }

            System.out.print("Номер кафедри: ");
            String deptInput = scanner.nextLine().trim();

            if (deptInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім!");
                continue;
            }

            try {
                int deptChoice = Integer.parseInt(deptInput);
                if (deptChoice >= 1 && deptChoice <= departments.size()) {
                    selectedDepartment = departments.get(deptChoice - 1);
                    departmentSelected = true;
                } else {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + departments.size());
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        List<Student> students = new ArrayList<>(studentRepository.findByDepartment(selectedDepartment));
        if (students.isEmpty()) {
            System.err.println("На кафедрі " + selectedDepartment.getName() + " немає студентів!");
            return;
        }

        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                int courseCompare = Integer.compare(s1.getCourse(), s2.getCourse());
                if (courseCompare != 0) {
                    return courseCompare;
                }
                return s1.getLastName().compareTo(s2.getLastName());
            }
        });

        System.out.println("\n=== Студенти кафедри " + selectedDepartment.getName() + " (за курсами) ===");
        int currentCourse = -1;
        for (Student student : students) {
            if (student.getCourse() != currentCourse) {
                currentCourse = student.getCourse();
                System.out.println("\n--- Курс " + currentCourse + " ---");
            }
            System.out.println("[ID: " + student.getId() + "] " + student.getFullName() +
                    ", Група: " + student.getGroup());
        }
        System.out.println("\nВсього студентів на кафедрі: " + students.size());
    }

    /**
     * Displays all students of a department, sorted alphabetically
     */
    public void showStudentsByDepartmentAlphabetically() {
        System.out.println("---Студенти кафедри за алфавітом---");

        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            System.err.println("Немає кафедр!");
            return;
        }

        Department selectedDepartment = null;
        boolean departmentSelected = false;

        while (!departmentSelected) {
            System.out.println("\n=== Оберіть кафедру ===");
            for (int i = 0; i < departments.size(); i++) {
                String facultyName = departments.get(i).getFaculty() != null ?
                        departments.get(i).getFaculty().getShortName() : "немає";
                System.out.println((i + 1) + ". " + departments.get(i).getName() + " (" + facultyName + ")");
            }

            System.out.print("Номер кафедри: ");
            String deptInput = scanner.nextLine().trim();

            if (deptInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім!");
                continue;
            }

            try {
                int deptChoice = Integer.parseInt(deptInput);
                if (deptChoice >= 1 && deptChoice <= departments.size()) {
                    selectedDepartment = departments.get(deptChoice - 1);
                    departmentSelected = true;
                } else {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + departments.size());
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        List<Student> students = new ArrayList<>(studentRepository.findByDepartment(selectedDepartment));
        if (students.isEmpty()) {
            System.err.println("На кафедрі " + selectedDepartment.getName() + " немає студентів!");
            return;
        }

        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                int lastNameCompare = s1.getLastName().compareTo(s2.getLastName());
                if (lastNameCompare != 0) {
                    return lastNameCompare;
                }
                int firstNameCompare = s1.getFirstName().compareTo(s2.getFirstName());
                if (firstNameCompare != 0) {
                    return firstNameCompare;
                }
                return s1.getMiddleName().compareTo(s2.getMiddleName());
            }
        });

        System.out.println("\n=== Студенти кафедри " + selectedDepartment.getName() + " (за алфавітом) ===");
        for (Student student : students) {
            System.out.println("[ID: " + student.getId() + "] " + student.getFullName() +
                    ", Курс: " + student.getCourse() +
                    ", Група: " + student.getGroup());
        }
        System.out.println("\nВсього студентів на кафедрі: " + students.size());
    }

    /**
     * Displays all teachers of a department, sorted alphabetically
     */
    public void showTeachersByDepartmentAlphabetically() {
        System.out.println("---Викладачі кафедри за алфавітом---");

        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            System.err.println("Немає кафедр!");
            return;
        }

        Department selectedDepartment = null;
        boolean departmentSelected = false;

        while (!departmentSelected) {
            System.out.println("\n=== Оберіть кафедру ===");
            for (int i = 0; i < departments.size(); i++) {
                String facultyName = departments.get(i).getFaculty() != null ?
                        departments.get(i).getFaculty().getShortName() : "немає";
                System.out.println((i + 1) + ". " + departments.get(i).getName() + " (" + facultyName + ")");
            }

            System.out.print("Номер кафедри: ");
            String deptInput = scanner.nextLine().trim();

            if (deptInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім!");
                continue;
            }

            try {
                int deptChoice = Integer.parseInt(deptInput);
                if (deptChoice >= 1 && deptChoice <= departments.size()) {
                    selectedDepartment = departments.get(deptChoice - 1);
                    departmentSelected = true;
                } else {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + departments.size());
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        List<Teacher> teachers = teacherRepository.findByDepartment(selectedDepartment);
        if (teachers.isEmpty()) {
            System.err.println("На кафедрі " + selectedDepartment.getName() + " немає викладачів!");
            return;
        }

        teachers.sort(new Comparator<Teacher>() {
            @Override
            public int compare(Teacher t1, Teacher t2) {
                int lastNameCompare = t1.getLastName().compareTo(t2.getLastName());
                if (lastNameCompare != 0) {
                    return lastNameCompare;
                }
                int firstNameCompare = t1.getFirstName().compareTo(t2.getFirstName());
                if (firstNameCompare != 0) {
                    return firstNameCompare;
                }
                return t1.getMiddleName().compareTo(t2.getMiddleName());
            }
        });

        System.out.println("\n=== Викладачі кафедри " + selectedDepartment.getName() + " (за алфавітом) ===");
        for (Teacher teacher : teachers) {
            System.out.println("[ID: " + teacher.getId() + "] " + teacher.getFullName() +
                    ", Посада: " + teacher.getPosition() +
                    ", Науковий ступінь: " + teacher.getAcademicDegree());
        }
        System.out.println("\nВсього викладачів на кафедрі: " + teachers.size());
    }

    /**
     * Displays all students of a department for a specified course (plain list)
     */
    public void showStudentsByDepartmentAndCourse() {
        System.out.println("---Студенти кафедри вказаного курсу---");

        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            System.err.println("Немає кафедр!");
            return;
        }

        Department selectedDepartment = null;
        boolean departmentSelected = false;

        while (!departmentSelected) {
            System.out.println("\n=== Оберіть кафедру ===");
            for (int i = 0; i < departments.size(); i++) {
                String facultyName = departments.get(i).getFaculty() != null ?
                        departments.get(i).getFaculty().getShortName() : "немає";
                System.out.println((i + 1) + ". " + departments.get(i).getName() + " (" + facultyName + ")");
            }

            System.out.print("Номер кафедри: ");
            String deptInput = scanner.nextLine().trim();

            if (deptInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім!");
                continue;
            }

            try {
                int deptChoice = Integer.parseInt(deptInput);
                if (deptChoice >= 1 && deptChoice <= departments.size()) {
                    selectedDepartment = departments.get(deptChoice - 1);
                    departmentSelected = true;
                } else {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + departments.size());
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        int course = 0;
        do {
            try {
                System.out.print("\nВведіть курс (1-6): ");
                String courseInput = scanner.nextLine().trim();
                if (courseInput.isEmpty()) {
                    System.err.println("Курс не може бути порожнім!");
                    continue;
                }
                course = Integer.parseInt(courseInput);
                if (course < 1 || course > 6) {
                    System.err.println("Курс має бути від 1 до 6!");
                    course = 0;
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
                course = 0;
            }
        } while (course == 0);

        List<Student> allStudents = studentRepository.findByDepartment(selectedDepartment);

        int count = 0;
        for (Student s : allStudents) {
            if (s.getCourse() == course) {
                count++;
            }
        }

        if (count == 0) {
            System.err.println("На кафедрі " + selectedDepartment.getName() +
                    " немає студентів на курсі " + course + "!");
            return;
        }

        Student[] filteredStudents = new Student[count];
        int index = 0;
        for (Student s : allStudents) {
            if (s.getCourse() == course) {
                filteredStudents[index++] = s;
            }
        }

        System.out.println("\n=== Студенти кафедри " + selectedDepartment.getName() +
                ", курс " + course + " ===");
        for (Student student : filteredStudents) {
            System.out.println("[ID: " + student.getId() + "] " + student.getFullName() +
                    ", Група: " + student.getGroup());
        }
        System.out.println("\nВсього студентів: " + filteredStudents.length);
    }

    /**
     * Displays all students of a department for a specified course (sorted alphabetically)
     */
    public void showStudentsByDepartmentAndCourseAlphabetically() {
        System.out.println("---Студенти кафедри вказаного курсу (за алфавітом)---");

        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            System.err.println("Немає кафедр!");
            return;
        }

        Department selectedDepartment = null;
        boolean departmentSelected = false;

        while (!departmentSelected) {
            System.out.println("\n=== Оберіть кафедру ===");
            for (int i = 0; i < departments.size(); i++) {
                String facultyName = departments.get(i).getFaculty() != null ?
                        departments.get(i).getFaculty().getShortName() : "немає";
                System.out.println((i + 1) + ". " + departments.get(i).getName() + " (" + facultyName + ")");
            }

            System.out.print("Номер кафедри: ");
            String deptInput = scanner.nextLine().trim();

            if (deptInput.isEmpty()) {
                System.err.println("Введення не може бути порожнім!");
                continue;
            }

            try {
                int deptChoice = Integer.parseInt(deptInput);
                if (deptChoice >= 1 && deptChoice <= departments.size()) {
                    selectedDepartment = departments.get(deptChoice - 1);
                    departmentSelected = true;
                } else {
                    System.err.println("Невірний вибір! Оберіть номер від 1 до " + departments.size());
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
            }
        }

        int course = 0;
        do {
            try {
                System.out.print("\nВведіть курс (1-6): ");
                String courseInput = scanner.nextLine().trim();
                if (courseInput.isEmpty()) {
                    System.err.println("Курс не може бути порожнім!");
                    continue;
                }
                course = Integer.parseInt(courseInput);
                if (course < 1 || course > 6) {
                    System.err.println("Курс має бути від 1 до 6!");
                    course = 0;
                }
            } catch (NumberFormatException e) {
                System.err.println("Невірний формат! Введіть число.");
                course = 0;
            }
        } while (course == 0);

        List<Student> allStudents = studentRepository.findByDepartment(selectedDepartment);

        int count = 0;
        for (Student s : allStudents) {
            if (s.getCourse() == course) {
                count++;
            }
        }

        if (count == 0) {
            System.err.println("На кафедрі " + selectedDepartment.getName() +
                    " немає студентів на курсі " + course + "!");
            return;
        }

        Student[] filteredStudents = new Student[count];
        int index = 0;
        for (Student s : allStudents) {
            if (s.getCourse() == course) {
                filteredStudents[index++] = s;
            }
        }

        Arrays.sort(filteredStudents, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                int lastNameCompare = s1.getLastName().compareTo(s2.getLastName());
                if (lastNameCompare != 0) {
                    return lastNameCompare;
                }
                int firstNameCompare = s1.getFirstName().compareTo(s2.getFirstName());
                if (firstNameCompare != 0) {
                    return firstNameCompare;
                }
                return s1.getMiddleName().compareTo(s2.getMiddleName());
            }
        });

        System.out.println("\n=== Студенти кафедри " + selectedDepartment.getName() +
                ", курс " + course + " (за алфавітом) ===");
        for (Student student : filteredStudents) {
            System.out.println("[ID: " + student.getId() + "] " + student.getFullName() +
                    ", Група: " + student.getGroup());
        }
        System.out.println("\nВсього студентів: " + filteredStudents.length);
    }
}