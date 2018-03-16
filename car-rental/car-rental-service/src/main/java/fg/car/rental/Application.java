package fg.car.rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

/**
 * Starts Application
 */
@SpringBootApplication
public class Application  implements WebMvcConfigurer {
    public static void main(String[] args) {
        //Set application timezone to UTC fixed avoiding errors coming from timezone differences
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Application.class, args);
    }

    /**
     * Add handler for static resources (html/css/js/etc)
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app/**").addResourceLocations("classpath:/static/");
    }


}
