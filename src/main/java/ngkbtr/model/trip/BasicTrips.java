package ngkbtr.model.trip;

public class BasicTrips {
    private String picture;
    private Trip cheapTrip;
    private Trip standardTrip;
    private Trip luxuryTrip;

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setCheapTrip(Trip cheapTrip) {
        this.cheapTrip = cheapTrip;
    }

    public void setStandardTrip(Trip standardTrip) {
        this.standardTrip = standardTrip;
    }

    public void setLuxuryTrip(Trip luxuryTrip) {
        this.luxuryTrip = luxuryTrip;
    }
}
