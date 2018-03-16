package fg.car.external.service;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarExternalServiceControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private Pattern pattern = Pattern.compile("\"timestamp\"\\:\".*?\",");

    @Test
    @SneakyThrows
    public void test() {

        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"County Code is mandatory!!!\",\"path\":\"/external/car/validate\"}"
                , pattern.matcher(send("/external/car/validate", "{}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Make is mandatory!!!\",\"path\":\"/external/car/validate\"}"
                , pattern.matcher(send("/external/car/validate", "{\"countryCodes\":[\"HU\"]}")).replaceAll(""));
        assertEquals("{\"responseCode\":\"NONUSABLE\"}"
                , pattern.matcher(send("/external/car/validate", "{\"countryCodes\":[\"DE\"],\"make\":\"Lada\"}")).replaceAll(""));
        assertEquals("{\"responseCode\":\"USABLE\"}"
                , pattern.matcher(send("/external/car/validate", "{\"countryCodes\":[\"RU\"],\"make\":\"Lada\"}")).replaceAll(""));
        assertEquals("{\"responseCode\":\"USABLE\"}"
                , pattern.matcher(send("/external/car/validate", "{\"countryCodes\":[\"HU\"],\"make\":\"Nissan\"}")).replaceAll(""));
    }

    private String send(String url, String requestText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestText, headers);

        String body = this.restTemplate.postForObject(url, request, String.class);

        return body;
    }


}
