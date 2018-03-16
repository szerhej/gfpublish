package fg.car.rental.service;

import fg.dev.car.bo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class of Car Rental Service
 */
@Controller
@RequestMapping("/services")
@Slf4j
public class CarRentalServiceController {

    @Autowired
    private CarService carService;


    /**
     * Returns all countries
     * @return
     */
    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public @ResponseBody
    List<Country> getCountries() {
        return carService.getCountries();
    }

    /**
     * Returns all cars
     * @return
     */
    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public @ResponseBody
    List<Car> getCars() {
        return carService.getCars();
    }

    /**
     * Returns details for a certain carId
     * @param carId Car identifier
     * @return
     */
    @RequestMapping(value = "/cars/{carId}", method = RequestMethod.GET)
    public @ResponseBody
    CarDetails getCarDetails(@PathVariable(name = "carId") Integer carId) {
        return carService.getCarDetails(carId);
    }

    /**
     * Book a car
     * @param carBooking
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cars/book", method = RequestMethod.POST)
    public @ResponseBody
    CarBookingResponse book(@RequestBody CarBooking carBooking) throws Exception {
        carService.book(carBooking);
        return CarBookingResponse.builder().accepted(Boolean.TRUE).build();

    }


}
