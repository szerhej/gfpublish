package fg.dev.car.bo;

import lombok.Data;

import java.util.List;

/**
 * Container for car database
 */
@Data
public class Cars {
    /**
     * List of available cars
     */
    private List<CarDetails> cars;
}
