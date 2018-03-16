package fg.dev.car.bo;

import lombok.Data;

import java.util.List;

@Data
public class ForeignUsageRequest {
    private String make;
    private List<String> countryCodes;
}
