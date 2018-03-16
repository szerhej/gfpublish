package fg.dev.car.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
/**
 * Class stores the details of a booking
 */
public class CarBooking {
    /**
     * Customer name
     */
    private String customerName;
    /**
     * Customer Address
     */
    private String customerAddress;
    /**
     * ID code (Driving license/ID/Passport)
     */
    private String customerID;
    /**
     * Car identifier
     */
    private Integer carId;
    /**
     * Date range from
     */
    private Date from;
    /**
     * Date range till
     */
    private Date to;
    /**
     * Usage of car (Domestic/Foreign)
     * This is not an enum since I did not want mvc reject on value
     */
    private String usage;
    /**
     * List of countries
     */
    private List<String> countries;

}
