import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ngkbtr.controller.request.*;
import ngkbtr.flowmanager.*;
import ngkbtr.model.User;
import ngkbtr.model.trip.BasicTrips;
import ngkbtr.model.trip.Trip;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Set;

@Ignore
public class TestApp {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void autocomplete(){
        /*AviasalesFlowManager service = new AviasalesFlowManager();
        GetCityAutocompleteRequest request = new GetCityAutocompleteRequest();
        request.setTerm("Моск");
        Set<CityAutocompleteObject> cityAutocomplete = service.getCityAutocomplete(new User("123"), request);
        for (CityAutocompleteObject obj: cityAutocomplete){
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

        /*TripFlowManager tripManager = new TripFlowManager(new AviasalesFlowManager(), new HotellookFlowManager(), new TripsterFlowManager());
        GetTripRequest request = new GetTripRequest();
        request.setSource("LED");
        request.setDestination("IEV");
        BasicTrips trips = tripManager.getBasicTripsByCity(new User("123"), request);
        //List<Trip> trips = tripManager.getBasicTripsByCountry(new User("123"), request);
        System.out.println(GSON.toJson(trips));*/

        /*AviasalesFlowManager service = new AviasalesFlowManager();
        RedirectUrlRequest request1 = new RedirectUrlRequest();
        request1.setFlight(trips.getCheapTrip().getFlight());
        RedirectUrlResponse redirectUrlResponse = service.redirectUserToBuyTicket(new User("123"), request1);
        System.out.println(redirectUrlResponse.getUrl());*/

        TripsterFlowManager tripsterFlowManager = new TripsterFlowManager();
        GetEntertainmentRequest request1 = new GetEntertainmentRequest();
        request1.setLocation("PAR");
        List<Entertainment> entertainments = tripsterFlowManager.getEntertainments(new User("123"), request1.getLocation(), null, null, null, 20L);
        System.out.println(GSON.toJson(entertainments));

    }
}
