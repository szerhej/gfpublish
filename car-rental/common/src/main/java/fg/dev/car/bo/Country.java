package fg.dev.car.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Country definition
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    /**
     * Country Code
     */
    private String countryCode;
    /**
     * Country Name
     */
    private String countryName;
}
