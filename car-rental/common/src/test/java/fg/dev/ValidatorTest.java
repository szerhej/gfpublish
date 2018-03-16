package fg.dev;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class ValidatorTest {
    @Test
    public void testNotNull() {
        Validator.notNull("aaa", "Not null");
        try {
            Validator.notNull(null, "Not null {}", Integer.valueOf(5));
            fail();
        } catch (ValidationException e) {
            assertEquals("Not null 5", e.getMessage());
        }
    }

    @Test
    public void testNull() {
        Validator.isNull(null, "Not null");
        try {
            Validator.isNull("aa", "Not null {}", Integer.valueOf(5));
            fail();
        } catch (ValidationException e) {
            assertEquals("Not null 5", e.getMessage());
        }
    }


    @Test
    public void testAssignableFrom() {
        Validator.assignableFrom("aa", String.class, "Not a string");
        Validator.assignableFrom(Integer.valueOf(5), Number.class, "Not a string");
        try {
            Validator.assignableFrom(null, String.class, "Not a string");
            fail();
        } catch (ValidationException e) {
            assertEquals("Not a string", e.getMessage());
        }
        try {
            Validator.assignableFrom(Integer.valueOf(5), String.class, "Not a string");
            fail();
        } catch (ValidationException e) {
            assertEquals("Not a string", e.getMessage());
        }

    }

    @Test
    public void testEquals() {
        Validator.equals("aabb", "aa" + "bb", "Not equals");
        Validator.equals(null, null, "Not equals");
        Validator.equals(Integer.valueOf(23423454), Integer.valueOf(23423454), "Not equals");
        try {
            Validator.equals(null, String.class, "Not a string");
            fail();
        } catch (ValidationException e) {
            assertEquals("Not a string", e.getMessage());
        }
        try {
            Validator.equals(Integer.valueOf(5), String.class, "Not a string");
            fail();
        } catch (ValidationException e) {
            assertEquals("Not a string", e.getMessage());
        }

    }

    @Test
    public void testNotEmpty() {
        Validator.notEmpty(Arrays.asList(5, 3), "Not empty");
        try {
            Validator.notEmpty(Collections.EMPTY_LIST, "Not empty");
            fail();
        } catch (ValidationException e) {
            assertEquals("Not empty", e.getMessage());
        }
        try {
            Validator.notEmpty(null, "Not empty");
            fail();
        } catch (ValidationException e) {
            assertEquals("Not empty", e.getMessage());
        }
    }

    @Test
    public void testNotBlank() {
        Validator.notBlank("  aa ", "Not blank");
        try {
            Validator.notBlank(null, "Not empty");
            fail();
        } catch (ValidationException e) {
            assertEquals("Not empty", e.getMessage());
        }
        try {
            Validator.notBlank("           ", "Not empty");
            fail();
        } catch (ValidationException e) {
            assertEquals("Not empty", e.getMessage());
        }
    }
}