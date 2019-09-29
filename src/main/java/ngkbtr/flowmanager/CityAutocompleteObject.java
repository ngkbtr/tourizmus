package ngkbtr.flowmanager;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CityAutocompleteObject {
    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("city_code")
    private String cityCode;
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("country_code")
    private String countryCode;

    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
