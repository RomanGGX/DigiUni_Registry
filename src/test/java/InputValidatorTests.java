import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ua.university.service.InputValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputValidatorTests {

    private InputValidator validator;

    @BeforeEach
    void setUp() {
        this.validator = new InputValidator();
    }

    @ParameterizedTest
    @ValueSource (strings = {"з пропуском", "-дефісНаПочатку", "ДефісНаприкінці-"})
    void checkWordShouldReturnNegative(String word) {
        assertEquals("-1", validator.checkWord(word));
    }

    @ParameterizedTest
    @CsvSource ({
            "якийсь-тАМ-чоЛОВІК-З-доВгИм-іМ'яМ, Якийсь-Там-Чоловік-З-Довгим-Ім'ям",
            "пРОСТИЙпРИКЛАД, Простийприклад",
            "а, А",
            "випадок-з-дефісом, Випадок-З-Дефісом",
            "ДужеДовгеІм'яЗВеликоюКількістюСимволівАБВГДЕЄЖЗ, Дужедовгеім'язвеликоюкількістюсимволівабвгдеєжз"
    })
    void checkWordShouldReturnFormatedWord(String argument, String expected) {
        assertEquals(expected, validator.checkWord(argument));
    }

    @ParameterizedTest
    @ValueSource (strings = {"tooMuchSymbols", "B15", "5A4", "a"})
    void checkGroupShouldReturnNegative(String group) {
        assertEquals("-1", validator.checkGroup(group));
    }

    @Test
    void checkGroupShouldReturnFormatedGroup() {
        assertEquals("21P", validator.checkGroup("21p"));
    }

    @ParameterizedTest
    @ValueSource (strings = {"TooManySymbols", "52937", "abcde", "a"})
    void checkStudentIDShouldReturnNegative(String studentID) {
        assertEquals("-1", validator.checkStudentID(studentID));
    }

    @ParameterizedTest
    @CsvSource ({
            "582aKe, 582AKE",
            "AbS502, ABS502",
            "5BE873, 5BE873",
            "729or7, 729OR7"
    })
    void checkStudentIDShouldReturnFormatedID (String argument, String expected) {
        assertEquals(expected, validator.checkStudentID(argument));
    }

    @ParameterizedTest
    @ValueSource (ints = {0, -2, 7, 10})
    void checkCourseShouldReturnNegative(int course) { assertEquals(-1, validator.checkCourse(course)); }

    @ParameterizedTest
    @ValueSource (ints = {1, 2, 3, 4, 5, 6})
    void checkCourseShouldReturnCourse(int course) { assertEquals(course, validator.checkCourse(course)); }

    @ParameterizedTest
    @ValueSource (ints = {1991, 1990, 15, 26, 1500})
    void checkYearEnrollShouldReturnNegative(int year) { assertEquals(-1, validator.checkYearEnroll(year)); }

    @ParameterizedTest
    @ValueSource (ints = {1992, 2000, 2023, 2015})
    void checkYearEnrollShouldReturnYear(int year) { assertEquals(year, validator.checkYearEnroll(year)); }

    @ParameterizedTest
    @ValueSource (strings = {"32.09.2015", "30.02.2015", "15.13.2015", "5.03.2015", "09.3.2015", "20.11.15"})
    void checkFullDateShouldReturnNegative(String date) { assertEquals("-1", validator.checkFullDate(date)); }

    @ParameterizedTest
    @CsvSource ({
            "15.10.2012, 15.10.2012",
            "29.02.2020, 29.02.2020",
            "31.12.2015, 31.12.2015"
    })
    void checkFullDateShouldReturnFormattedFullDate(String argument, String expected) {
        assertEquals(expected, validator.checkFullDate(argument));
    }

    @Test
    void checkStudyFormShouldReturnNegative() { assertEquals("-1", validator.checkStudyForm("Щось інше")); }

    @ParameterizedTest
    @CsvSource ({
            "Бюджет, бюджет",
            "КОНТРАКТ, контракт",
            "бюджет, бюджет"
    })
    void checkStudyFormShouldReturnFormattedStudyForm(String argument, String expected) {
        assertEquals(expected, validator.checkStudyForm(argument));
    }

    @Test
    void checkAcademicDegreeShouldReturnNegative() { assertEquals("-1", validator.checkAcademicDegree("Щось інше")); }

    @ParameterizedTest
    @CsvSource ({
            "ДОКТОР ФІЛОСОФІЇ, доктор філософії",
            "ДоКтОр нАуК, доктор наук"
    })
    void checkAcademicDegreeShouldReturnFormattedAcademicDegree(String argument, String expected) {
        assertEquals(expected, validator.checkAcademicDegree(argument));
    }

    @Test
    void checkAcademicTitleShouldReturnNegative() { assertEquals("-1", validator.checkAcademicTitle("Щось інше")); }

    @ParameterizedTest
    @CsvSource ({
            "ДОЦЕНТ, доцент",
            "старшиЙ дослідниК, старший дослідник",
            "ПрОфЕсОр, професор"
    })
    void checkAcademicTitleShouldReturnFormattedAcademicTitle(String argument, String expected) {
        assertEquals(expected, validator.checkAcademicTitle(argument));
    }

    @Test
    void checkStatusShouldReturnNegative() { assertEquals("-1", validator.checkStatus("Щось інше")); }

    @ParameterizedTest
    @CsvSource ({
            "НАВЧАЄТЬСЯ, навчається",
            "академвідпусткА, академвідпустка",
            "ВіДрАхОвАнИй, відрахований"
    })
    void checkStatusShouldReturnFormattedStatus(String argument, String expected) {
        assertEquals(expected, validator.checkStatus(argument));
    }

    @ParameterizedTest
    @ValueSource (strings = {".invalid_email@gmail.com", "cool_email@gmail.", "even_cooler..email@gmail.com"})
    void checkEmailShouldReturnNegative(String argument) { assertEquals("-1", validator.checkEmail(argument)); }

    @ParameterizedTest
    @CsvSource ({
            "The.Real_Slim.Shady228@gmail.gmail.gmail.com, the.real_slim.shady228@gmail.gmail.gmail.com",
            "cOOl1UsEr2nAmE3And4EmAIl5dOwnlOAd6wAlpApErs@ukr.net.any.other.domain.will.not.work.haha, cool1user2name3and4email5download6walpapers@ukr.net.any.other.domain.will.not.work.haha"
    })
    void checkEmailShouldReturnFormattedEmail(String argument, String expected) {
        assertEquals(expected, validator.checkEmail(argument));
    }

    @ParameterizedTest
    @ValueSource (strings = {"+3805", "2942429185", "+1929401938592929485820", "Suddenly a dog"})
    void checkPhoneNumberShouldReturnNegative(String argument) { assertEquals("-1", validator.checkPhoneNumber(argument)); }

    @ParameterizedTest
    @ValueSource (strings = {"+3802819572", "+502918", "+39102859018472"})
    void checkPhoneNumberShouldReturnPhoneNumber(String argument) { assertEquals(argument, validator.checkPhoneNumber(argument)); }
}