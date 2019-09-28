package ngkbtr.flowmanager;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CityAutocompleteObject {
    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("city_code")
    private String cityCode;

    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityAutocompleteObject that = (CityAutocompleteObject) o;
        return Objects.equals(cityName, that.cityName) &&
                Objects.equals(cityCode, that.cityCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cityName, cityCode);
    }

    @Override
    public String toString() {
        return "CityAutocompleteObject{" +
                "cityName='" + cityName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }
}
