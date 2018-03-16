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

@Service
@Slf4j
public class ExternalServiceClient {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Value("${fg.car.rental.service.external.service.url}")
    private String externalSercviceURL;

    private HttpClient client;

    @PostConstruct
    public void init() {
        client = HttpClientBuilder.create().build();
    }

    public boolean isUsable(String make, List<String> countryCodes) {
        HttpPost httpPost = new HttpPost(externalSercviceURL);
        httpPost.setHeader("Content-Type", "application/json");

        ForeignUsageRequest foreignUsageRequest = new ForeignUsageRequest();
        foreignUsageRequest.setCountryCodes(countryCodes);
        foreignUsageRequest.setMake(make);


        httpPost.setEntity(LangUtils.sneakyThrows(() -> new StringEntity(objectMapper.writeValueAsString(foreignUsageRequest))));
        HttpResponse httpResponse = null;
        try{
            httpResponse=client.execute(httpPost);
        } catch (Exception e){
            log.error("External Service is not available!!!",e);
            Validator.fail("External Service is not available!!!");
        }
        HttpResponse httpResponse1=httpResponse;
        Validator.isTrue(httpResponse.getStatusLine().getStatusCode()==200,"External Service is not available!!!");
        return LangUtils.sneakyThrows(() -> {
            try (InputStream is = LangUtils.sneakyThrows(() -> httpResponse1.getEntity().getContent())) {
                ForeignUsageResponse foreignUsageResponse = objectMapper.readValue(IOUtils.toString(is, "UTF8"), ForeignUsageResponse.class);
                return foreignUsageResponse.getResponseCode() == ForeignUsageResponseType.USABLE;
            }
        });


    }

}
