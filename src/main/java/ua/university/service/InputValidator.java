package ua.university.service;

public class InputValidator {

    /**
     * Adds input validator methods to the class
     */
    public InputValidator () {}

    /**
     * Returns "-1" if argument is not a word
     * @param word String word to check
     * @return String word or "-1"
     */
    public String isWord (String word) {
        for (int i=0; i<word.length(); i++) {
            if (!Character.isLetter(word.charAt(i)) && word.charAt(i) != '-' && word.charAt(i) != '\'') return "-1";
        }
        return word;
    }

    /**
     * Returns "-1" if argument is not a group ID
     * @param group String group ID to check
     * @return String group ID or "-1"
     */
    public String isGroup (String group) {
        if (group.length() != 3) return "-1";
        else if (!Character.isDigit(group.charAt(0))
                || !Character.isDigit(group.charAt(1))
                || !Character.isLetter(group.charAt(2))) return "-1";
        return group;
    }

    /**
     * Returns "-1" if argument is not a student ID
     * @param studentID String student ID to check
     * @return String student ID or "-1"
     */
    public String isStudentID (String studentID) {
        if (studentID.length() != 6) return "-1";
        for (int i=0; i<6; i++) {
            if (!Character.isDigit(studentID.charAt(i))
                    && !Character.isLetter(studentID.charAt(i))) return "-1";
        }
        return studentID;
    }
}