package fg.dev;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import java.util.List;

/**
 * Validator used by application to perform data validation
 */
@Slf4j
public class Validator {

    public static void assignableFrom(Object object, Class clazz, String message, Object... args) {
        if (object == null || !clazz.isAssignableFrom(object.getClass())) {
            fail(message, args);
        }
    }


    public static void equals(Object o1, Object o2, String message, Object... args) {
        if (o1 != null && !o1.equals(o2)) {
            fail(message, args);
        }
        if (o1 == null && o2 != null) {
            fail(message, args);
        }
    }

    public static void notEmpty(List list, String message, Object... args) {
        if (list == null || list.isEmpty()) {
            fail(message, args);
        }
    }

    public static void notNull(Object o, String message, Object... args) {
        if (o == null) {
            fail(message, args);
        }
    }

    public static void isNull(Object o, String message, Object... args) {
        if (o != null) {
            fail(message, args);
        }
    }

    public static void isTrue(boolean b, String message, Object... args) {
        if (!b) {
            fail(message, args);
        }
    }

    public static void isFalse(boolean b, String message, Object... args) {
        if (b) fail(message, args);
    }


    public static void notBlank(CharSequence str, String message, Object... args) {
        if (StringUtils.isBlank(str)) {
            fail(message, args);
        }
    }


    public static <T> T fail(String message, Object... args) {
        String txt = MessageFormatter.arrayFormat(message, args).getMessage();
        log.error(txt);
        throw new ValidationException(txt);
    }

}