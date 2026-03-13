package ua.university.repository;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;

import java.util.*;


public class StudentRepository implements Repository<Student, Integer>{

    private final Map<Integer, Student> studentsById = new LinkedHashMap<>();
    private final Set<String> emailIndex = new HashSet<>();

    @Override
    public List<Student> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(studentsById.values()));
    }

    /**
     * Checks if the student with the same full name is presented
     * @param names String array of first, middle and last names
     * @return Optional student if it finds, and optional empty if not
     */
    public Optional<Student> FindByFullName (String[] names) {
        for (Student student : studentsById.values()) {
            if (student.getFirstName().equalsIgnoreCase(names[0])) {
                if (student.getMiddleName().equalsIgnoreCase(names[1])) {
                    if (student.getLastName().equalsIgnoreCase(names[2])) return Optional.of(student);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Student> findById(Integer id) {
        return Optional.ofNullable(studentsById.get(id));
    }

    /**
     * Makes a list of students of the same course
     * @param course int course
     * @return List of students by course
     */
    public List<Student> FindByCourse (int course) {
        List<Student> result = new ArrayList<>();

        for (Student student : studentsById.values()) {
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

        for (Student student : studentsById.values()) {
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
        for (Student student : studentsById.values()) {
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
        for (Student student : studentsById.values()) {
            if (email.equalsIgnoreCase(student.getEmail())) return Optional.of(student);
        }
        return Optional.empty();
    }

    public List<Student> findByFaculty(Faculty faculty) {
        List<Student> result = new ArrayList<>();
        for (Student student : studentsById.values()) {
            if (student.getDepartment() != null &&
                    student.getDepartment().getFaculty() != null &&
                    student.getDepartment().getFaculty().getCode() == faculty.getCode()) {
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> findByDepartment(Department department) {
        List<Student> result = new ArrayList<>();
        for (Student student : studentsById.values()) {
            if (student.getDepartment() != null &&
                    student.getDepartment().getCode() == department.getCode()) {
                result.add(student);
            }
        }
        return result;
    }

    /**
     * Adds a new student to the repository.
     * Creates a new array with increased size and appends the student to the end.
     * @param student the student to be added
     */
    @Override
    public void add(Student student) {
        studentsById.put(student.getId(), student);
        emailIndex.add(student.getEmail().toLowerCase());
    }

    @Override
    public boolean deleteById(Integer id) {
        Student removed = studentsById.remove(id);
        if (removed == null) return false;
        emailIndex.remove(removed.getEmail().toLowerCase());
        return true;
    }

    public boolean deleteByFullName(String[] names) {
        Optional<Student> studentOpt = FindByFullName(names);
        if (studentOpt.isPresent()) {
            return deleteById(studentOpt.get().getId());
        }
        return false;
    }

    @Override
    public boolean update(Integer id, Student updated) {
        Student existing = studentsById.get(id);
        if (existing == null) return false;

        String oldEmail = existing.getEmail().toLowerCase();
        String newEmail = updated.getEmail().toLowerCase();

        if (!oldEmail.equals(newEmail)) {
            if (emailIndex.contains(newEmail)) {
                throw new IllegalArgumentException(
                        "Email '" + updated.getEmail() + "' вже використовується.");
            }
            emailIndex.remove(oldEmail);
            emailIndex.add(newEmail);
        }

        studentsById.put(id, updated);
        return true;
    }

    /**
     * Generates the next available ID for a new student.
     * Finds the maximum ID in the repository and returns it incremented by 1.
     * @return the next available student ID
     */
    public int getNextId() {
        int maxId = 0;
        for (int id : studentsById.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }
}