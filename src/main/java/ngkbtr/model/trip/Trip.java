package ngkbtr.model.trip;

import ngkbtr.flowmanager.Entertainment;
import ngkbtr.flowmanager.EntertainmentsCnt;
import ngkbtr.flowmanager.FlightDirection;
import ngkbtr.flowmanager.Hotel;

import java.math.BigDecimal;
import java.util.List;

public class Trip {
    private TripClass tripClass;
    private String picture;
    private String destination;
    private BigDecimal averagePrice;

    private FlightDirection flight;
    private List<Hotel> hotels;
    private List<Entertainment> entertainments;

    public TripClass getTripClass() {
        return tripClass;
    }

    public String getPicture() {
        return picture;
    }

    public String getDestination() {
        return destination;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public FlightDirection getFlight() {
        return flight;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public List<Entertainment> getEntertainment() {
        return entertainments;
    }

    public static final class Builder {
        private TripClass tripClass;
        private String picture;
        private String destination;
        private BigDecimal averagePrice;
        private FlightDirection flight;
        private List<Hotel> hotels;
        private List<Entertainment> entertainments;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder setTripClass(TripClass tripClass) {
            this.tripClass = tripClass;
            return this;
        }

        public Builder setPicture(String picture) {
            this.picture = picture;
            return this;
        }

        public Builder setDestination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder setAveragePrice(BigDecimal averagePrice) {
            this.averagePrice = averagePrice;
            return this;
        }

        public Builder setFlight(FlightDirection flight) {
            this.flight = flight;
            return this;
        }

        public Builder setHotels(List<Hotel> hotels) {
            this.hotels = hotels;
            return this;
        }

        public Builder setEntertainment(List<Entertainment> entertainments) {
            this.entertainments = entertainments;
            return this;
        }

        public Trip build() {
            Trip trip = new Trip();
            trip.destination = this.destination;
            trip.flight = this.flight;
            trip.averagePrice = this.averagePrice;
            trip.hotels = this.hotels;
            trip.picture = this.picture;
            trip.tripClass = this.tripClass;
            trip.entertainments = this.entertainments;
            return trip;
        }
    }
}
