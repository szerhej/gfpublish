package fg.car.external.service;

import fg.dev.car.bo.ForeignUsageRequest;
import fg.dev.car.bo.ForeignUsageResponse;
import fg.dev.car.bo.ForeignUsageResponseType;
import fg.dev.Validator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * Mock controller fro external service
 */
@Controller
public class CarExternalServiceController {

    /**
     * Countries where Lada models can be used
     */
    List<String> ladaCountries = Arrays.asList("RU", "HU", "AL", "AM", "BY", "UA");

    /**
     * Service perfoms car validation for certain countries
     * @param usageRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/external/car/validate", method = RequestMethod.POST)
    public @ResponseBody
    ForeignUsageResponse checkCarForeignUsability(@RequestBody ForeignUsageRequest usageRequest) throws Exception {

        //Simulates waiting for 1 sec
        Thread.sleep(1000);
        //Validates Country Code
        Validator.notNull(usageRequest.getCountryCodes(), "County Code is mandatory!!!");
        //Validates Car brand
        Validator.notNull(usageRequest.getMake(), "Make is mandatory!!!");
        for(String countryCode:usageRequest.getCountryCodes()){
            //If a car is Lada and will be used in a Non-Lada country return negative response
            if ("Lada".equalsIgnoreCase(usageRequest.getMake()) && !ladaCountries.contains(countryCode.toUpperCase())) {
                return new ForeignUsageResponse(ForeignUsageResponseType.NONUSABLE);
            }
        }
        //Positive response
        return new ForeignUsageResponse(ForeignUsageResponseType.USABLE);

    }

}
