package fg.dev.car.bo;

import lombok.Data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
/**
 * Container class for countries
 */
public class Countries {
    /**
     * List of countries
     */
    private List<Country> countries;

    public Country search(String countryCode) {
        if (countryCode == null) return null;
        int pos = Collections.binarySearch(countries, Country.builder().countryCode(countryCode.toUpperCase()).build(), Comparator.comparing((o) -> o.getCountryCode()));
        return pos >= 0 ? countries.get(pos) : null;
    }

}
