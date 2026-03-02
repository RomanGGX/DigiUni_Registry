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
}