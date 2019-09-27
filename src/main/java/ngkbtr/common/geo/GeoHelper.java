package ngkbtr.common.geo;

import ngkbtr.model.User;
import ngkbtr.model.location.Location;

import java.util.ArrayList;
import java.util.List;

public class GeoHelper {

    public static <T extends User> List<T> getFilteredLocationObjects(List<T> objects, Location center, Integer radius){

        List<T> result = new ArrayList<>();
        for(T obj: objects) {
            Location loc = obj.getLocationInfo().getLocation();
            Double distance = 2 * Math.sin(Math.sqrt(Math.pow(Math.sin(Math.toRadians((loc.getLatitude() - center.getLatitude()) / 2)), 2) +
                    Math.cos(Math.toRadians(loc.getLatitude())) * Math.cos(Math.toRadians(center.getLatitude())) *
                            Math.pow(Math.sin(Math.toRadians((loc.getLongitude() - center.getLongitude()) / 2)), 2))) * 6378245;
            if(distance <= radius){
                result.add(obj);
            }
        }

        return result;
        /*return objects.stream()
                .filter(n -> Math.pow(n.getLocation().getLatitude() - center.getLatitude(), 2) + Math.pow(n.getLocation().getLongitude() - center.getLongitude(), 2) <= Math.pow(radius, 2))
                .collect(Collectors.toList());*/
    }
}
