package fg.dev.car.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignUsageResponse {
    private ForeignUsageResponseType responseCode;
}
