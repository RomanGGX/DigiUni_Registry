package ua.university.service;

public class InputValidator {

    /**
     * Adds input validator methods to the class
     */
    public InputValidator () {}

    /**
     * Turns command into int array code and returns -1 if the command does not exist
     * @param userTokens String array if user input line tokens
     * @return int array of a command
     */
    public int[] commandValidator (String[] userTokens) {
        int[] commandCode = new int[5];

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
}