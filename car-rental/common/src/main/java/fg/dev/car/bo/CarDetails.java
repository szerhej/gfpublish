package fg.dev.car.bo;

import lombok.Data;

@Data
/**
 * Detailed info of car
 * num property defines how many cars are available from this car type.
 */
public class CarDetails extends Car {
    /**
     * Car classification
     */
    private String classification;
    /**
     * Type of car body
     */
    private String bodyType;
    /**
     * Number of doors
     */
    private Integer doors;
    /**
     * Number of seats
     */
    private Integer seats;
    /**
     * Defines ho many cars are available to rent
     */
    private Integer num;
}
