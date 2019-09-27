package ngkbtr.model.location;

import java.io.Serializable;

public class LocationInfo implements IHasLocation, Serializable {

    private Location location;

    public LocationInfo(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
