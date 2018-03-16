package fg.dev.car.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Response container for the car booking service
 */
@Getter
@Builder
public class CarBookingResponse {
    @NonNull
    private Boolean accepted;
}
