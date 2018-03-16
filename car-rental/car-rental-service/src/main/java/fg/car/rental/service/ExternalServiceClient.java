package fg.car.rental.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fg.dev.LangUtils;
import fg.dev.Validator;
import fg.dev.car.bo.ForeignUsageRequest;
import fg.dev.car.bo.ForeignUsageResponse;
import fg.dev.car.bo.ForeignUsageResponseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

/**
 * external service client implementation
 */
@Service
@Slf4j
public class ExternalServiceClient {

    /**
     * ObjectMapper to parse the response
     */
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * External service url defined by property
     */
    @Value("${fg.car.rental.service.external.service.url}")
    private String externalSercviceURL;

    /**
     * Http client used to access external service
     */
    private HttpClient client;

    @PostConstruct
    public void init() {
        client = HttpClientBuilder.create().build();
    }

    /**
     * This method checks if the car can be used in certain countries calling an external service
     * @param make
     * @param countryCodes
     * @return
     */
    public boolean isUsable(String make, List<String> countryCodes) {
        HttpPost httpPost = new HttpPost(externalSercviceURL);
        httpPost.setHeader("Content-Type", "application/json");

        ForeignUsageRequest foreignUsageRequest = new ForeignUsageRequest();
        foreignUsageRequest.setCountryCodes(countryCodes);
        foreignUsageRequest.setMake(make);


        httpPost.setEntity(LangUtils.sneakyThrows(() -> new StringEntity(objectMapper.writeValueAsString(foreignUsageRequest))));
        HttpResponse httpResponse = null;
        try{
            //Call external service
            httpResponse=client.execute(httpPost);
        } catch (Exception e){
            log.error("External Service is not available!!!",e);
            Validator.fail("External Service is not available!!!");
        }
        HttpResponse httpResponse1=httpResponse;
        //If response code is not 200 then service is not available
        //Of course the response code can be other than 200 even in case successful response but that scenario is not handled here
        Validator.isTrue(httpResponse.getStatusLine().getStatusCode()==200,"External Service is not available!!!");

        //Gain the return value from the response
        return LangUtils.sneakyThrows(() -> {
            try (InputStream is = LangUtils.sneakyThrows(() -> httpResponse1.getEntity().getContent())) {
                ForeignUsageResponse foreignUsageResponse = objectMapper.readValue(IOUtils.toString(is, "UTF8"), ForeignUsageResponse.class);
                return foreignUsageResponse.getResponseCode() == ForeignUsageResponseType.USABLE;
            }
        });


    }

}
