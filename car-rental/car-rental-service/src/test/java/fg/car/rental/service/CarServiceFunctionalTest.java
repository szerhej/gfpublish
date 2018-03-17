package fg.car.rental.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
public class CarServiceFunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private Pattern pattern = Pattern.compile("\"timestamp\"\\:\".*?\",");

    @PostConstruct
    public void init() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    @SneakyThrows
    public void testBooking() {

        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Customer Name is mandatory!!!\",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Only minute is valid!!!\",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerEmail\":\"a@a.com\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"DOMESTIC\",\"from\":1521126720000,\"to\":1521123920000}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Minimum rent time: 1 hour!!!\",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerEmail\":\"a@a.com\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"DOMESTIC\",\"from\":1521126000000,\"to\":1521126060000}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Invalid Period\",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerEmail\":\"a@a.com\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"DOMESTIC\",\"from\":1521126060000,\"to\":1521126000000}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Usage has invalid value: aaa!!! Valid Values: DOMESTIC and FOREIGN\",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerEmail\":\"a@a.com\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"aaa\",\"from\":"+sysTimestamp(1)+",\"to\":"+sysTimestamp(3)+"}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"In case of foreign usage country code must be specified!!!\",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerEmail\":\"a@a.com\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"FOREIGN\",\"from\":"+sysTimestamp(1)+",\"to\":"+sysTimestamp(3)+"}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Unknown Country: KG!!! \",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerEmail\":\"a@a.com\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"FOREIGN\",\"countries\":[\"DE\",\"KG\"],\"from\":"+sysTimestamp(1)+",\"to\":"+sysTimestamp(3)+"}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Email is invalid!!!\",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerEmail\":\"a.com\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"FOREIGN\",\"countries\":[\"KG\"],\"from\":"+sysTimestamp(1)+",\"to\":"+sysTimestamp(3)+"}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Email is mandatory!!!\",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"FOREIGN\",\"countries\":[\"KG\"],\"from\":"+sysTimestamp(1)+",\"to\":"+sysTimestamp(3)+"}")).replaceAll(""));

        assertEquals("{\"accepted\":true}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerEmail\":\"a@a.com\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"DOMESTIC\",\"from\":"+sysTimestamp(1)+",\"to\":"+sysTimestamp(3)+"}")).replaceAll(""));
        assertEquals("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"This car type is not available in this period!!!\",\"path\":\"/services/cars/book\"}"
                , pattern.matcher(send("/services/cars/book", "{\"customerName\":\"John Smith\",\"customerEmail\":\"a@a.com\",\"customerAddress\":\"Kobago utca 3\",\"customerID\":\"AXC\",\"carId\":1,\"usage\":\"DOMESTIC\",\"from\":"+sysTimestamp(1)+",\"to\":"+sysTimestamp(3)+"}")).replaceAll(""));
    }

    private long sysTimestamp(long addHours){
        long l=System.currentTimeMillis()/1000L/60L+addHours*60L;
        return l*1000L*60L;
    }

    private String send(String url, String requestText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestText, headers);

        String body = this.restTemplate.postForObject(url, request, String.class);

        return body;
    }


}
