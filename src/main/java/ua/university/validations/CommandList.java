package ua.university.validations;

public enum CommandList {
    // Commands
    show("anyone", 0),
    add("admin", 0),
    delete("admin", 0),
    move("personal", 0),    // Moves student to different groups/courses/faculties
    enable("anyone", 0),
    exit("anyone", 0),
    end("anyone", 0),

    // Command parameters
    department("anyone", 1),     // Unchanged
    faculty("anyone", 1),
    student("anyone", 1),
    teacher("anyone", 1),
    university("anyone", 1),

    // Second command parameters
    brief("personal", 2);

    /** Defines the permission level of a command */
    private final String permission;
    /** Defines a position of a word */
    private final int position;

    CommandList (String permission, int position) {
        this.permission = permission;
        this.position = position;
    }

    /**
     * Gets command permission
     * @return String permission
     */
    public String getPermission() {return permission;}

    /**
     * Gets command position
     * @return int position
     */
    public int getPosition() {return position;}
}
