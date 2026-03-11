package ua.university.ui;

public class CommandContainer {

    private final String message;
    private final Runnable action;
    private final int accessLevel;

    public CommandContainer(String message, Runnable action, int accessLevel) {
        this.message = message;
        this.action = action;
        this.accessLevel = accessLevel;
    }

    public String getMessage() {return message;}

    public Runnable getAction() {return action;}

    public int getAccessLevel() {return accessLevel;}

    /**
     * Checks if access level of command is lower than arg
     * @param arg int access level
     * @return boolean result of arg >= accessLevel operation
     */
    public boolean isAvailable(int arg) {
        return arg >= accessLevel;
    }
}
