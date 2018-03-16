package fg.dev.car.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class CarBookingResponse {
    @NonNull
    private Boolean accepted;
}
