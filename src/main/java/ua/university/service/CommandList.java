package ua.university.service;

public enum CommandList {
    // Commands
    show("anyone", 1),
    login("anyone", 2),
    exit("anyone", 3),
    end("anyone", 4),
    find("anyone", 5),

    add("service", 6),
    delete("service", 7),
    move("service", 8),    // Moves student to different groups/courses/faculties
    update("service", 9),

    create("admin", 10),

    // Command parameters
    department("anyone", 11),
    faculty("anyone", 12),
    student("anyone", 13),
    teacher("anyone", 14),
    university("anyone", 15),
    by("anyone", 16),

    user("admin", 17),

    // Second command parameters
    full_name("anyone", 18),
    course("anyone", 19),
    group("anyone", 20),
    student_id("anyone", 21),
    email("anyone", 22);

    /** Defines the permission level of a command */
    private final String permission;

    /** Provides every command with unique ID code */
    private final int enumID;

    CommandList (String permission, int enumID) {
        this.permission = permission;
        this.enumID = enumID;
    }

    /**
     * Gets command permission
     * @return String permission
     */
    public String getPermission() {return permission;}

    /**
     * Gets enum ID of an element
     * @return int enum ID
     */
    public int getEnumID() {return enumID;}
}
