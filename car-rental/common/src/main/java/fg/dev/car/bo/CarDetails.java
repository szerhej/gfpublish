package fg.dev.car.bo;

import lombok.Data;

@Data
public class CarDetails extends Car {
    private String classification;
    private String bodyType;
    private Integer doors;
    private Integer seats;
    private Integer num;
}
