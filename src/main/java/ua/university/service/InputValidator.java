package ua.university.service;

public class InputValidator {

    /**
     * Adds input validator methods to the class
     */
    public InputValidator () {}

    /**
     * Turns command into int array code and returns -1 if the command does not exist
     * @param userTokens String array if user input line tokens
     * @param tokensNumber int length of the int array
     * @return int array of a command
     */
    public int[] commandValidator (String[] userTokens, int tokensNumber) {
        int[] commandCode = new int[tokensNumber+1];

        for (int i=0; i<userTokens.length; i++){
            try {
                commandCode[i] = CommandList.valueOf(userTokens[i]).getEnumID();
            } catch (IllegalArgumentException e) {
                commandCode[i] = -1;
                return commandCode;
            }
        }

        return commandCode;
    }

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

    public String isGroup (String group) {
        if (group.length() != 3) return "-1";
        else if (!Character.isDigit(group.charAt(0))
                || !Character.isDigit(group.charAt(1))
                || !Character.isLetter(group.charAt(2))) return "-1";
        return group;
    }

    public String isStudentID (String studentID) {
        if (studentID.length() != 6) return "-1";
        for (int i=0; i<6; i++) {
            if (!Character.isDigit(studentID.charAt(i))
                    && !Character.isLetter(studentID.charAt(i))) return "-1";
        }
        return studentID;
    }
}