package fg.dev;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * Helper class to share often used methods
 */
public class LangUtils {

    /**
     * Load resource from classpath
     * @param src Source file path
     * @return
     */
    public static String getResource(String src) {
        return getResource(LangUtils.class, src);
    }

    /**
     * Load resource from classpath
     * @param clazz Class used to relativly define resource path
     * @param src Source file path
     * @return
     */
    public static String getResource(Class clazz, String src) {
        return sneakyThrows(() -> {
            try (InputStream is = clazz.getResourceAsStream(src)) {
                return IOUtils.toString(is, "UTF8");
            }
        });
    }

    /**
     * Callback Interface which drops (checked/non-checked) exception
     * @param <T>
     */
    public interface CallBack<T> {
        T call() throws Exception;
    }

    /**
     * Method is used to catch checked/non-checked exceptions on standard way for all application
     * @param callBack  Usually Lambda expression to call back
     * @param <T> Return type
     * @return Returned value by callback
     */
    public static <T> T sneakyThrows(CallBack<T> callBack) {
        try {
            //Callback
            return callBack.call();
        } catch (ValidationException|AppException e) {
            //Do not transfer any application exception
            throw e;
        } catch (Exception e) {
            //Transfer unknown exception to unchecked AppException
            throw new AppException(e);
        }
    }
}
