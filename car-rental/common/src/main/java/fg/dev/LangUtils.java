package fg.dev;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class LangUtils {

    public static String getResource(String src) {
        return getResource(LangUtils.class, src);
    }

    public static String getResource(Class clazz, String src) {
        return sneakyThrows(() -> {
            try (InputStream is = clazz.getResourceAsStream(src)) {
                return IOUtils.toString(is, "UTF8");
            }
        });
    }

    public interface CallBack<T> {
        T call() throws Exception;
    }

    public static <T> T sneakyThrows(CallBack<T> callBack) {
        try {
            return callBack.call();
        } catch (ValidationException|AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}
