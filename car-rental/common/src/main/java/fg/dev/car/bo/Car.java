package fg.dev.car.bo;

import lombok.Data;

@Data
/**
 * Basic BO for car. This class is used for list
 */
public class Car {
    /**
     * ID of Car
     */
    private Integer id;
    /**
     * Brand of car
     */
    private String make;
    /**
     * Car model
     */
    private String model;
    /**
     * Car Version
     */
    private String version;
}
