package ua.university.repository;

import ua.university.domain.*;
import java.util.*;

public class TeacherRepository implements Repository<Teacher, Integer> {

    private final Map<Integer, Teacher> teachersById = new LinkedHashMap<>();
    private final Set<String> emailIndex = new HashSet<>();

    @Override
    public List<Teacher> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(teachersById.values()));
    }

    @Override
    public Optional<Teacher> findById(Integer id) {
        return Optional.ofNullable(teachersById.get(id));
    }


    public Optional<Teacher> findByFullName(String[] names) {
        for (Teacher teacher : teachersById.values()) {
            if (teacher.getFirstName().equalsIgnoreCase(names[0]) &&
                    teacher.getMiddleName().equalsIgnoreCase(names[1]) &&
                    teacher.getLastName().equalsIgnoreCase(names[2])) {
                return Optional.of(teacher);
            }
        }
        return Optional.empty();
    }

    public Optional<Teacher> findByEmail(String email) {
        for (Teacher teacher : teachersById.values()) {
            if (teacher.getEmail().equalsIgnoreCase(email)) {
                return Optional.of(teacher);
            }
        }
        return Optional.empty();
    }

    public List<Teacher> findByFaculty(Faculty faculty) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher teacher : teachersById.values()) {
            if (teacher.getDepartment() != null &&
                    teacher.getDepartment().getFaculty() != null &&
                    teacher.getDepartment().getFaculty().getCode() == faculty.getCode()) {
                result.add(teacher);
            }
        }
        return result;
    }

    public List<Teacher> findByDepartment(Department department) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher teacher : teachersById.values()) {
            if (teacher.getDepartment() != null &&
                    teacher.getDepartment().getCode() == department.getCode()) {
                result.add(teacher);
            }
        }
        return result;
    }

    public List<Teacher> findByPosition(String position) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher teacher : teachersById.values()) {
            if (teacher.getPosition().equalsIgnoreCase(position)) {
                result.add(teacher);
            }
        }
        return result;
    }

    @Override
    public void add(Teacher teacher) {
        teachersById.put(teacher.getId(), teacher);
        emailIndex.add(teacher.getEmail().toLowerCase());
    }

    @Override
    public boolean deleteById(Integer id) {
        Teacher removed = teachersById.remove(id);
        if (removed == null) return false;
        emailIndex.remove(removed.getEmail().toLowerCase());
        return true;
    }

    @Override
    public boolean update(Integer id, Teacher updatedTeacher) {
        Teacher current = teachersById.get(id);
        if (current == null) return false;

        String oldEmail = current.getEmail().toLowerCase();
        String newEmail = updatedTeacher.getEmail().toLowerCase();
        if (!oldEmail.equals(newEmail) && emailIndex.contains(newEmail)) {
            throw new IllegalArgumentException("Викладач з email '" + updatedTeacher.getEmail() + "' вже існує.");
        }

        emailIndex.remove(oldEmail);
        emailIndex.add(newEmail);
        teachersById.put(id, updatedTeacher);
        return true;
    }

    public int getNextId() {
        int maxId = 0;
        for (int id : teachersById.keySet()) {
            if (id > maxId)
                maxId = id;
        }
        return maxId + 1;
    }
}