package ua.university.service;

public class InputValidator {
    /** Defines and saves a current year */
    private final int currentYear = 2026;

    /**
     * Adds input validator methods to the class
     */
    public InputValidator () {}

    /**
     * Returns "-1" if argument is not a word
     * @param word String word to check
     * @return String word (formated) or "-1"
     */
    public String checkWord(String word) {
        if (word.isEmpty()) return "-1";

        for (int i=0; i<word.length(); i++) {
            if (!Character.isLetter(word.charAt(i)) && word.charAt(i) != '-' && word.charAt(i) != '\'') return "-1";
        }

        if (word.length() == 1 && Character.isLetter(word.charAt(0))) return word.toUpperCase();

        String result = "";

        for (int i=0; i < word.length(); i++) {
            if (i == 0) {
                if (word.charAt(0) == '\'' || word.charAt(0) == '-') return "-1";
                result += Character.toUpperCase(word.charAt(0));
            } else if (i == word.length()-1) {
                if (word.charAt(word.length()-1) == '\'' || word.charAt(word.length()-1) == '-') return "-1";
            } else if (Character.isLetter(word.charAt(i-1)) && Character.isLetter(word.charAt(i))) {
                result += Character.toLowerCase(word.charAt(i));
            } else if ( word.charAt(i-1) == '-' && Character.isLetter(word.charAt(i))) {
                result += Character.toUpperCase(word.charAt(i));
            } else if ((word.charAt(i-1) == '\'' || word.charAt(i-1) == '-')
                && (word.charAt(i) == '\'' || word.charAt(i) == '-')) {
                return "-1";
            } else result += word.charAt(i);
        }

        return result;
    }

    /**
     * Returns "-1" if argument is not a group ID
     * @param group String group ID to check
     * @return String group ID (formated) or "-1"
     */
    public String checkGroup(String group) {
        if (group.length() != 3) return "-1";
        else if (!Character.isDigit(group.charAt(0))
                || !Character.isDigit(group.charAt(1))
                || !Character.isLetter(group.charAt(2))) return "-1";
        return group.toUpperCase();
    }

    /**
     * Returns "-1" if argument is not a student ID
     * @param studentID String student ID to check
     * @return String student ID (formated) or "-1"
     */
    public String checkStudentID(String studentID) {
        if (studentID.length() != 6) return "-1";
        for (int i=0; i<6; i++) {
            if (!Character.isDigit(studentID.charAt(i))
                    && !Character.isLetter(studentID.charAt(i))) return "-1";
        }
        return studentID.toUpperCase();
    }

    /**
     * Returns "-1" if argument is not a course number
     * @param course int course number
     * @return int course or "-1"
     */
    public int checkCourse (int course) {
        if (course < 1 || course > 6) return -1;
        return course;
    }

    /**
     * Returns "-1" if argument is higher than the current year
     * @param yearEnroll int enrollment year
     * @return int yearEnroll or "-1"
     */
    public int checkYearEnroll (int yearEnroll) {
        if (yearEnroll > currentYear || yearEnroll < 1992) return -1;
        return yearEnroll;
    }

    /**
     * Returns "-1" if argument is not a date
     * @param date String date
     * @param lowestYear int the lowest possible year
     * @param highestYear int the highest possible year
     * @return String date (formated) or "-1"
     */
    public String checkFullDate (String date, int lowestYear, int highestYear) {
        if (date.length() != 10) return "-1";

        String result = "";

        for (int i=0; i < date.length(); i++) {
            if (i == 2 || i == 5) {
                if (date.charAt(i) == ','
                        || date.charAt(i) == '.') result += '.';
                else return "-1";
            } else if (!Character.isDigit(date.charAt(i))) {
                return "-1";
            } else result += date.charAt(i);
        }

        int day = Integer.parseInt(result.substring(0, 2));
        int month = Integer.parseInt(result.substring(3, 5));
        int year = Integer.parseInt(result.substring(6));

        if (day > 31 || day < 1) return "-1";
        else if (month > 12 || month < 1) return "-1";
        else if (year < lowestYear) return "-1";
        else if (year > highestYear && month > 8) return "-1";
        else if (month == 2 && year%4 == 0 && day > 29) return "-1";
        else if (month == 2 && day > 28) return "-1";
        else if (month < 8 && month%2 == 0 && day > 30) return "-1";
        else if (month > 7 && month%2 == 1 && day > 30) return "-1";

        return result;
    }

    /**
     * *OVERLOADING* Returns "-1" if argument is not a date. Default year limit: lowestYear-currentYear
     * @param date String date
     * @param lowestYear int the lowest possible year
     * @return String date (formated) or "-1"
     */
    public String checkFullDate (String date, int lowestYear) {
        return checkFullDate(date, lowestYear, currentYear);
    }

    /**
     * *OVERLOADING* Returns "-1" if argument is not a date. Default year limit: 1901-currentYear
     * @param date String date
     * @return String date (formated) or "-1"
     */
    public String checkFullDate (String date) {
        return checkFullDate(date, 1901);
    }

    /**
     * Returns "-1" if argument is not a study form
     * @param studyForm String study form
     * @return String study form (formated) or "-1"
     */
    public String checkStudyForm (String studyForm) {
        if (studyForm.isEmpty()) return "-1";

        if (studyForm.equalsIgnoreCase("бюджет")) return "бюджет";
        else if (studyForm.equalsIgnoreCase("контракт")) return "контракт";

        return "-1";
    }

    /**
     * Returns "-1" if argument is not an academic degree
     * @param academicDegree String academic degree
     * @return String academic degree (formated) or "-1"
     */
    public String checkAcademicDegree (String academicDegree) {
        if (academicDegree.isEmpty()) return "-1";

        if (academicDegree.equalsIgnoreCase("доктор філософії")) return "доктор філософії";
        else if (academicDegree.equalsIgnoreCase("доктор наук")) return "доктор наук";

        return "-1";
    }

    /**
     * Returns "-1" if argument is not an academic title
     * @param academicTitle String academic title
     * @return String academic title (formated) or "-1"
     */
    public String checkAcademicTitle (String academicTitle) {
        if (academicTitle.isEmpty()) return "-1";

        if (academicTitle.equalsIgnoreCase("доцент")) return "доцент";
        else if (academicTitle.equalsIgnoreCase("старший дослідник")) return "старший дослідник";
        else if (academicTitle.equalsIgnoreCase("професор")) return "професор";

        return "-1";
    }

    /**
     * Returns "-1" if argument is not a status
     * @param status String status
     * @return String status (formated) or "-1"
     */
    public String checkStatus (String status) {
        if (status.isEmpty()) return "-1";

        if (status.equalsIgnoreCase("навчається")) return "навчається";
        else if (status.equalsIgnoreCase("академвідпустка")) return "академвідпустка";
        else if (status.equalsIgnoreCase("відрахований")) return "відрахований";

        return "-1";
    }

    /**
     * Returns "-1" if argument is not an email
     * @param email String email
     * @return String email (formated) or "-1"
     */
    public String checkEmail (String email) {
        if (email.isEmpty()) return "-1";

        boolean atSingPresent = false;

        for (int i=0; i < email.length(); i++) {
            if (atSingPresent) {
                if (email.charAt(i) == '.' && i != email.length()-1) return email.toLowerCase();
            } else if (email.charAt(i) == '@') atSingPresent = true;
        }

        return "-1";
    }

    /**
     * Returns "-1" if argument is not a number
     * @param phoneNumber String phone number
     * @return String phone number or "-1"
     */
    public String checkPhoneNumber (String phoneNumber) {
        if (phoneNumber.length() > 15 || phoneNumber.length() < 7) return "-1";

        if (phoneNumber.charAt(0) != '+') return "-1";

        for (int i=1; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i))) return "-1";
        }

        return phoneNumber;
    }
}