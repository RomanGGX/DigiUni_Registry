package ua.university.repository;
import ua.university.domain.User;
import java.util.*;

public class UserRepository {

    private final Set<String> usernames = new HashSet<>();
    private final Map<String, String> credentials = new HashMap<>();
    private final Map<String, Integer> accessLevels = new HashMap<>();

    public UserRepository() {
        add(new User("user1", "userPass", 1));
        add(new User("manager1", "managerPass", 2));
        add(new User("admin1", "adminPass", 3));
    }

    public boolean add(User user) {
        if(usernames.contains(user.getUsername())) {
            return false;
        }
        usernames.add(user.getUsername());
        credentials.put(user.getUsername(), user.getPassword());
        accessLevels.put(user.getUsername(), user.getAccessLevel());
        return true;
    }

    public boolean checkIfExists(String username, String password) {
        return usernames.contains(username) && credentials.get(username).equals(password);
    }

    public int getAccessLevel(String username) {
        return accessLevels.get(username);
    }

}