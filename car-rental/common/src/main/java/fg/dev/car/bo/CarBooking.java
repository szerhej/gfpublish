package fg.dev.car.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CarBooking {
    private String customerName;
    private String customerAddress;
    private String customerID;
    private Integer carId;
    private Date from;
    private Date to;
    private String usage;
    private List<String> countries;

}
