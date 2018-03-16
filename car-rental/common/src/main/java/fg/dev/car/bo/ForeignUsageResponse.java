package fg.dev.car.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * External service response
 * Of course error messages and other properties could be included
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignUsageResponse {
    /**
     * Response code
     */
    private ForeignUsageResponseType responseCode;
}
