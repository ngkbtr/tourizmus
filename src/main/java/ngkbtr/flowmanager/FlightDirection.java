package ngkbtr.flowmanager;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Objects;

public class FlightDirection {

    private BigDecimal value;
    private Long trip_duration;
    private String signature;
    private String search_id;
    private String return_date;
    private String origin_name;
    private String origin;
    private String order_url_id;
    private Long number_of_changes;
    private String gate;
    private String found_at;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private transient Flight[][] flights;
    private Long duration;
    private Boolean direct;
    private String destination_name;
    private String destination;
    private String depart_date;
    private String airline;

    public BigDecimal getValue() {
        return value;
    }

    public Long getTrip_duration() {
        return trip_duration;
    }

    public String getSignature() {
        return signature;
    }

    public String getSearch_id() {
        return search_id;
    }

    public String getReturn_date() {
        return return_date;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public String getOrigin() {
        return origin;
    }

    public String getOrder_url_id() {
        return order_url_id;
    }

    public Long getNumber_of_changes() {
        return number_of_changes;
    }

    public String getGate() {
        return gate;
    }

    public String getFound_at() {
        return found_at;
    }

    public Flight[][] getFlights() {
        return flights;
    }

    public Long getDuration() {
        return duration;
    }

    public Boolean getDirect() {
        return direct;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepart_date() {
        return depart_date;
    }

    public String getAirline() {
        return airline;
    }

    static class Flight{
       private String origin;
       private String number;
       private Long duration;
       private String destination;
       private Long departure;
       private Long delay;
       private Long arrival;
       private String airline;
       private String aircraft;

        public String getOrigin() {
            return origin;
        }

        public String getNumber() {
            return number;
        }

        public Long getDuration() {
            return duration;
        }

        public String getDestination() {
            return destination;
        }

        public Long getDeparture() {
            return departure;
        }

        public Long getDelay() {
            return delay;
        }

        public Long getArrival() {
            return arrival;
        }

        public String getAirline() {
            return airline;
        }

        public String getAircraft() {
            return aircraft;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightDirection that = (FlightDirection) o;
        return Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination);
    }
}
