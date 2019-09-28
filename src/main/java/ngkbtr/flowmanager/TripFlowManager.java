package ngkbtr.flowmanager;

import ngkbtr.controller.request.GetTripRequest;
import ngkbtr.model.User;
import ngkbtr.model.trip.BasicTrips;
import ngkbtr.model.trip.Trip;
import ngkbtr.model.trip.TripClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TripFlowManager {

    private static final String PICTURE_URL = "https://photo.hotellook.com/static/cities/400x300/%s.jpg";

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private final AviasalesFlowManager aviasalesFlowManager;
    private final HotellookFlowManager hotellookFlowManager;
    private final TripsterFlowManager tripsterFlowManager;

    @Autowired
    public TripFlowManager(AviasalesFlowManager aviasalesFlowManager, HotellookFlowManager hotellookFlowManager, TripsterFlowManager tripsterFlowManager) {
        this.aviasalesFlowManager = aviasalesFlowManager;
        this.hotellookFlowManager = hotellookFlowManager;
        this.tripsterFlowManager = tripsterFlowManager;
    }

    public BasicTrips getBasicTrips(User user, GetTripRequest request){

        List<FlightDirection> flights = aviasalesFlowManager.getBasicDirectionParameters(request.getSource(), request.getDestination()).stream().sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(flights)){
            throw new RuntimeException("Any airplane offers have not been found");
        }

        System.out.println(flights);

        BigDecimal averageFlightPriceCheap = new BigDecimal(flights.stream().mapToDouble(n -> n.getValue().doubleValue()).average().orElse(Double.NaN));

        List<FlightDirection> sortedFlights = flights.stream().filter(n -> n.getValue().compareTo(getCheapMaxAveragePrice(averageFlightPriceCheap)) <= 0).sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());

        FlightDirection flight = sortedFlights.isEmpty() ? flights.get(0) : sortedFlights.get(new Random().nextInt(sortedFlights.size()));

        List<Hotel> hotels = hotellookFlowManager.getBasicHotels(request.getDestination(), flight.getDepart_date(), flight.getReturn_date()).stream().sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(hotels)){
            throw new RuntimeException("Any hotel offers have not been found");
        }

        BigDecimal averageHotelPriceCheap = new BigDecimal(hotels.stream().filter(m -> m.getStars() <= 3).mapToDouble(n -> n.getPriceAvg().doubleValue()).average().orElse(Double.NaN));
        List<Hotel> sortedHotels = hotels.stream().filter(n -> n.getPriceFrom().compareTo(getCheapMaxAveragePrice(averageHotelPriceCheap)) <= 0 && n.getStars() <= 3).sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());
        Trip cheapTrip = Trip.Builder.builder()
                .setTripClass(TripClass.CHEAP)
                .setFlight(flight)
                .setHotels(sortedHotels.isEmpty() ? Collections.singletonList(hotels.get(0)) : sortedHotels)
                .setAveragePrice(averageFlightPriceCheap.add(averageHotelPriceCheap))
                .setDestination(request.getDestination())
                .setPicture(String.format(PICTURE_URL, request.getDestination()))
                .build();


        BigDecimal averageFlightPriceStandard = new BigDecimal(flights.stream().mapToDouble(n -> n.getValue().doubleValue()).average().orElse(Double.NaN));

        sortedFlights = flights.stream().filter(n -> n.getValue().compareTo(getCheapMaxAveragePrice(averageFlightPriceStandard)) > 0 &&
                n.getValue().compareTo(getLuxuryMinAveragePrice(averageFlightPriceStandard)) <= 0).sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());

        flight = sortedFlights.isEmpty() ? flights.get(0) : sortedFlights.get(new Random().nextInt(sortedFlights.size()));

        hotels = hotellookFlowManager.getBasicHotels(request.getDestination(), flight.getDepart_date(), flight.getReturn_date());
        if(CollectionUtils.isEmpty(hotels)){
            throw new RuntimeException("Any hotel offers have not been found");
        }

        BigDecimal averageHotelPriceStandard = new BigDecimal(hotels.stream().filter(m -> m.getStars() >= 3 && m.getStars() <= 4).mapToDouble(n -> n.getPriceAvg().doubleValue()).average().orElse(Double.NaN));

        sortedHotels = hotels.stream().filter(n -> n.getPriceFrom().compareTo(getCheapMaxAveragePrice(averageHotelPriceStandard)) > 0 &&
                n.getPriceAvg().compareTo(getLuxuryMinAveragePrice(averageHotelPriceStandard)) <= 0).sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());
        Trip standardTrip = Trip.Builder.builder()
                .setTripClass(TripClass.STANDARD)
                .setFlight(flight)
                .setHotels(sortedHotels.isEmpty() ? Collections.singletonList(hotels.get(0)) : sortedHotels)
                .setAveragePrice(averageFlightPriceStandard.add(averageHotelPriceStandard))
                .setDestination(request.getDestination())
                .setPicture(String.format(PICTURE_URL, request.getDestination()))
                .build();

        BigDecimal averageFlightPriceLuxury = new BigDecimal(flights.stream().mapToDouble(n -> n.getValue().doubleValue()).average().orElse(Double.NaN));

        sortedFlights = flights.stream().filter(FlightDirection::getDirect).sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());

        flight = sortedFlights.isEmpty() ? flights.get(flights.size() - 1) : sortedFlights.get(new Random().nextInt(sortedFlights.size()));

        hotels = hotellookFlowManager.getBasicHotels(request.getDestination(), flight.getDepart_date(), flight.getReturn_date());
        if(CollectionUtils.isEmpty(hotels)){
            throw new RuntimeException("Any hotel offers have not been found");
        }

        BigDecimal averageHotelPriceLuxury = new BigDecimal(hotels.stream().filter(m -> m.getStars() == 5).mapToDouble(n -> n.getPriceAvg().doubleValue()).average().orElse(Double.NaN));

        sortedHotels = hotels.stream().filter(n -> n.getPriceAvg().compareTo(getLuxuryMinAveragePrice(averageHotelPriceLuxury)) > 0).sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());
        Trip luxuryTrip = Trip.Builder.builder()
                .setTripClass(TripClass.LUXURY)
                .setFlight(flight)
                .setHotels(sortedHotels.isEmpty() ? Collections.singletonList(hotels.get(0)) : sortedHotels)
                .setAveragePrice(averageFlightPriceLuxury.add(averageHotelPriceLuxury))
                .setDestination(request.getDestination())
                .setPicture(String.format(PICTURE_URL, request.getDestination()))
                .build();

        BasicTrips result = new BasicTrips();
        result.setCheapTrip(cheapTrip);
        result.setStandardTrip(standardTrip);
        result.setLuxuryTrip(luxuryTrip);
        result.setPicture(String.format(PICTURE_URL, request.getDestination()));

        return result;
    }


    //lower than 40%
    private static BigDecimal getCheapMaxAveragePrice(BigDecimal averagePrice){
        return averagePrice.multiply(new BigDecimal(40L)).divide(ONE_HUNDRED);
    }

    //higher than 70%
    private static BigDecimal getLuxuryMinAveragePrice(BigDecimal averagePrice){
        return averagePrice.multiply(new BigDecimal(70L)).divide(ONE_HUNDRED);
    }
}
