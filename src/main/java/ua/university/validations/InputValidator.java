package ua.university.validations;

public class InputValidator {

    /**
     * Adds input validator methods to the class
     */
    public InputValidator () {}

    public boolean commandValidator (String userToken, int accessLevel, int position) {
        try {
            CommandList commandList = CommandList.valueOf(userToken);
            if (commandList.getPosition() == position){
                if (commandList.getPermission().equals("anyone")) return true;
                else if (commandList.getPermission().equals("personal") && accessLevel > 0) return true;
                else return commandList.getPermission().equals("admin") && accessLevel == 2;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return false;
    }
}