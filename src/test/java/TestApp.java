import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ngkbtr.controller.request.GetFlightsRequest;
import ngkbtr.controller.request.GetHotelsRequest;
import ngkbtr.controller.request.GetTripRequest;
import ngkbtr.flowmanager.*;
import ngkbtr.model.User;
import ngkbtr.model.trip.BasicTrips;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

@Ignore
public class TestApp {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void autocomplete(){
        /*AviasalesFlowManager service = new AviasalesFlowManager();
        GetFlightsRequest request = new GetFlightsRequest();
        request.setSource("LED");
        request.setDestination("MOW");
        request.setStartDate("2019-10-26");
        request.setDuration("10");
        List<FlightDirection> directionParameters = service.getDirectionParameters(new User("123"), request);
        for (Object obj: directionParameters){
            System.out.println(GSON.toJson(obj));
        }*/

        /*HotellookFlowManager hservice = new HotellookFlowManager();

        GetHotelsRequest hrequest = new GetHotelsRequest();
        hrequest.setLocation("MOW");
        hrequest.setStartDate("2019-10-10");
        hrequest.setEndDate("2019-10-11");
        hrequest.setMaxPricePerNight(new BigDecimal(20000L));
        hrequest.setMinPricePerNight(new BigDecimal(10000L));
        hrequest.setStars(5L);
        List<Hotel> hotels = hservice.getHotels(new User("123"), hrequest);
        for (Hotel hotel: hotels){
            System.out.println(GSON.toJson(hotel));
        }*/

        TripFlowManager tripManager = new TripFlowManager(new AviasalesFlowManager(), new HotellookFlowManager(), new TripsterFlowManager());
        GetTripRequest request = new GetTripRequest();
        request.setSource("MOW");
        request.setDestination("FR");
        BasicTrips trips = tripManager.getBasicTrips(new User("123"), request);
        System.out.println(GSON.toJson(trips));
    }
}
