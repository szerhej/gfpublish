package fg.car.rental.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import fg.dev.LangUtils;
import fg.dev.Validator;
import fg.dev.car.bo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Car service to maintain data on service level
 */
@Service
@Slf4j
public class CarService {

    /**
     * Country database
     */
    private Countries countries;

    /**
     * Car database
     */
    private Cars cars;

    /**
     * Car booking database. List inside is not thread safe
     */
    private Map<Integer, List<CarBooking>> carBookingMap = new ConcurrentHashMap<>();

    @Value("${fg.car.rental.service.minimumRentTime:1}")
    private long minimumRentTime;

    @Autowired
    private ExternalServiceClient externalServiceClient;

    /**
     * Initializes service database (countries/cars)
     *
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        //Load Countries
        countries = objectMapper.readValue(LangUtils.getResource("/countries.json"), Countries.class);
        countries.getCountries().sort(Comparator.comparing(Country::getCountryCode));

        //Load Cars
        cars = objectMapper.readValue(LangUtils.getResource("/cars.json"), Cars.class);

        //Assigning car ids
        int i = 0;
        for (Car car : cars.getCars()) {
            car.setId(i++);
        }
    }

    /**
     * Returns all countries
     *
     * @return
     */
    public List<Country> getCountries() {
        return countries.getCountries();
    }

    /**
     * Returns all cars
     *
     * @return
     */
    public List<Car> getCars() {
        return Lists.transform(cars.getCars(), carDetails ->
                LangUtils.sneakyThrows(() -> {
                    Car car = new Car();
                    BeanUtils.copyProperties(car, carDetails);
                    return car;
                })
        );
    }

    /**
     * Returns Car details defined by carId
     *
     * @param carId
     * @return
     */
    public CarDetails getCarDetails(Integer carId) {
        Validator.notNull(carId, "Car ID is mandatory!!!");
        Validator.isTrue(carId >= 0 && carId < cars.getCars().size(), "Invalid Car ID: {}!!!", carId);
        return cars.getCars().get(carId);
    }

    /**
     * Car booking service
     * It also validates the request
     *
     * @param carBooking
     */
    public void book(CarBooking carBooking) {
        Validator.notBlank(carBooking.getCustomerName(), "Customer Name is mandatory!!!");
        Validator.notBlank(carBooking.getCustomerAddress(), "Customer Address is mandatory!!!");
        Validator.notBlank(carBooking.getCustomerID(), "Customer ID is mandatory!!!");
        Validator.notNull(carBooking.getCarId(), "Car ID is mandatory!!!");
        Validator.notNull(carBooking.getUsage(), "Usage is mandatory!!!");
        Validator.notNull(carBooking.getFrom(), "Period is mandatory!!!");
        Validator.notNull(carBooking.getTo(), "Period is mandatory!!!");
        Validator.notNull(carBooking.getCustomerEmail(), "Email is mandatory!!!");
        Validator.isTrue(EmailValidator.getInstance().isValid(carBooking.getCustomerEmail()), "Email is invalid!!!");
        Validator.isTrue(carBooking.getFrom().getTime() % (1000L * 60L) == 0, "Only minute is valid!!!");
        Validator.isTrue(carBooking.getTo().getTime() % (1000L * 60L) == 0, "Only minute is valid!!!");
        Validator.isTrue(carBooking.getTo().getTime() > carBooking.getFrom().getTime(), "Invalid Period");
        Validator.isTrue(carBooking.getTo().getTime() - 1000L * 60L * 60L * minimumRentTime > carBooking.getFrom().getTime(), "Minimum rent time: {} hour!!!", minimumRentTime);
        Validator.isTrue(carBooking.getFrom().getTime() > System.currentTimeMillis(), "You can book only for future!!!");

        Usage usage = Usage.resolve(carBooking.getUsage());

        Validator.notNull(usage, "Usage has invalid value: {}!!! Valid Values: {} and {}", carBooking.getUsage(), Usage.DOMESTIC, Usage.FOREIGN);

        Validator.isTrue(usage == Usage.DOMESTIC || usage == Usage.FOREIGN && carBooking.getCountries() != null && carBooking.getCountries().size() > 0,
                "In case of foreign usage country code must be specified!!!");


        CarDetails carDetails = cars.getCars().get(carBooking.getCarId());

        //Validate if country codes are valid!!!
        validateCountries(carBooking.getCountries());

        //Validate if car can be used in all of these countries!!!
        Validator.isTrue(usage == Usage.DOMESTIC || usage == Usage.FOREIGN && externalServiceClient.isUsable(carDetails.getMake(), carBooking.getCountries()),
                "This model cannot be used in all of the specified countries!!!");

        //This list is not thread safe, cannot be accessed unsync way
        List<CarBooking> carBookings = carBookingMap.computeIfAbsent(carBooking.getCarId(), integer -> new ArrayList<>());

        //sync on carBookings list otherwise to make the process thread safe
        synchronized (carBookings) {
            //Find all bookings for this car type for the defined period
            Collection<CarBooking> bookings = Collections2.filter(carBookings, carBooking1 -> carBooking.getCarId().equals(carBooking1.getCarId())
                    && carBooking.getFrom().getTime() <= carBooking1.getTo().getTime()
                    && carBooking1.getFrom().getTime() <= carBooking.getTo().getTime());

            //This is the key part to check if the car is available
            //Since there can be more cars from the same type it lets allocate more than one from a certain type
            Validator.isTrue(bookings.size() < carDetails.getNum(), "This car type is not available in this period!!!");

            //Saves to booking database
            carBookings.add(carBooking);
            log.info("Car Booked:{} {}", carDetails, carBooking);
        }
    }

    private void validateCountries(List<String> countryCodes) {
        if (countryCodes != null) {
            countryCodes.forEach(countryCode -> Validator.notNull(countries.search(countryCode), "Unknown Country: {}!!! ", countryCode));
        }
    }


}
