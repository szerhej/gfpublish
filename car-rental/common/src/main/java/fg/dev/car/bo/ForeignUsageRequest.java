package fg.dev.car.bo;

import lombok.Data;

import java.util.List;

/**
 * Foreign service request definition
 * We cannot reuse other beans since foreign service is independent 3rd party service
 * We can use the same class for the client as well as the mock
 */
@Data
public class ForeignUsageRequest {
    /**
     * Car brand
     */
    private String make;
    /**
     * List of country codes
     */
    private List<String> countryCodes;
}
