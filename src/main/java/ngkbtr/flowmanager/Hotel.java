package ngkbtr.flowmanager;

import java.math.BigDecimal;

public class Hotel {

    private String hotelId;
    private String hotelName;
    private BigDecimal priceAvg;
    private HotelLocation location;
    private Long stars;
    private BigDecimal priceFrom;
    private String locationId;

    public String getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public BigDecimal getPriceAvg() {
        return priceAvg;
    }

    public HotelLocation getLocation() {
        return location;
    }

    public Long getStars() {
        return stars;
    }

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public String getLocationId() {
        return locationId;
    }

    static class HotelLocation{
        private String name;
        private String country;
        private String state;
        private HotelLocation location;
        private HotelGeoposition geo;

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public String getState() {
            return state;
        }

        public HotelLocation getLocation() {
            return location;
        }

        public HotelGeoposition getGeo() {
            return geo;
        }
    }

    static class HotelGeoposition {
        private Double lat;
        private Double lon;

        public Double getLat() {
            return lat;
        }

        public Double getLon() {
            return lon;
        }
    }
}
