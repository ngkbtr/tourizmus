package ngkbtr.controller.request;

import ngkbtr.flowmanager.FlightDirection;

public class RedirectUrlRequest {
    private FlightDirection flight;

    public FlightDirection getFlight() {
        return flight;
    }

    public void setFlight(FlightDirection flight) {
        this.flight = flight;
    }
}
