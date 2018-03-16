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

@Controller
public class CarExternalServiceController {

    List<String> ladaCountries = Arrays.asList("RU", "HU", "AL", "AM", "BY", "UA");

    @RequestMapping(value = "/external/car/validate", method = RequestMethod.POST)
    public @ResponseBody
    ForeignUsageResponse checkCarForeignUsability(@RequestBody ForeignUsageRequest usageRequest) throws Exception {

        Thread.sleep(1000);
        Validator.notNull(usageRequest.getCountryCodes(), "County Code is mandatory!!!");
        Validator.notNull(usageRequest.getMake(), "Make is mandatory!!!");
        for(String countryCode:usageRequest.getCountryCodes()){
            if ("Lada".equalsIgnoreCase(usageRequest.getMake()) && !ladaCountries.contains(countryCode.toUpperCase())) {
                return new ForeignUsageResponse(ForeignUsageResponseType.NONUSABLE);
            }
        }
        return new ForeignUsageResponse(ForeignUsageResponseType.USABLE);

    }

}
