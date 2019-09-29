package ngkbtr.flowmanager;

import ngkbtr.controller.request.GetTripRequest;
import ngkbtr.model.User;
import ngkbtr.model.trip.BasicTrips;
import ngkbtr.model.trip.Trip;
import ngkbtr.model.trip.TripClass;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
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

    public BasicTrips getBasicTripsByCity(User user, GetTripRequest request){

        List<FlightDirection> flights = aviasalesFlowManager.getBasicDirectionParameters(request.getSource(), request.getDestination()).stream().sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(flights)){
            throw new RuntimeException("Any airplane offers have not been found");
        }

        //CHEAP

        BigDecimal averageFlightPriceCheap = new BigDecimal(flights.stream().mapToDouble(n -> n.getValue().doubleValue()).average().orElse(Double.NaN));
        List<FlightDirection> sortedFlights = flights.stream().filter(n -> n.getValue().compareTo(getCheapMaxAveragePrice(averageFlightPriceCheap)) <= 0).sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());
        FlightDirection flight = sortedFlights.isEmpty() ? flights.get(0) : sortedFlights.get(new Random().nextInt(sortedFlights.size()));

        List<Hotel> hotels = hotellookFlowManager.getBasicHotels(flight.getDestination(), flight.getDepart_date(), flight.getReturn_date()).stream().sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(hotels)){
            throw new RuntimeException("Any hotel offers have not been found");
        }
        BigDecimal averageHotelPriceCheap = new BigDecimal(hotels.stream().filter(m -> m.getStars() <= 3).mapToDouble(n -> n.getPriceAvg().doubleValue()).average().orElse(Double.NaN));
        List<Hotel> sortedHotels = hotels.stream().filter(n -> n.getPriceFrom().compareTo(getCheapMaxAveragePrice(averageHotelPriceCheap)) <= 0 && n.getStars() <= 3).sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());

        List<Entertainment> sortedEntertainments = tripsterFlowManager.getEntertainments(user, flight.getDestination(), null, null, null, null);
        List<Entertainment> shufledEntertainment = sortedEntertainments.stream().limit(sortedEntertainments.size()/3).collect(Collectors.toList());

        Trip cheapTrip = Trip.Builder.builder()
                .setTripClass(TripClass.CHEAP)
                .setFlight(flight)
                .setHotels(sortedHotels.isEmpty() ? Collections.singletonList(hotels.get(0)) : sortedHotels)
                .setEntertainment(Collections.unmodifiableList(shufledEntertainment))
                .setAveragePrice(averageFlightPriceCheap.add(averageHotelPriceCheap))
                .setDestination(flight.getDestination())
                .setPicture(String.format(PICTURE_URL, flight.getDestination()))
                .build();

        //STANDARD

        BigDecimal averageFlightPriceStandard = new BigDecimal(flights.stream().mapToDouble(n -> n.getValue().doubleValue()).average().orElse(Double.NaN));

        sortedFlights = flights.stream().filter(n -> n.getValue().compareTo(getCheapMaxAveragePrice(averageFlightPriceStandard)) > 0 &&
                n.getValue().compareTo(getLuxuryMinAveragePrice(averageFlightPriceStandard)) <= 0).sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());

        flight = sortedFlights.isEmpty() ? flights.get(0) : sortedFlights.get(new Random().nextInt(sortedFlights.size()));

        hotels = hotellookFlowManager.getBasicHotels(flight.getDestination(), flight.getDepart_date(), flight.getReturn_date());
        if(CollectionUtils.isEmpty(hotels)){
            throw new RuntimeException("Any hotel offers have not been found");
        }

        BigDecimal averageHotelPriceStandard = new BigDecimal(hotels.stream().filter(m -> m.getStars() >= 3 && m.getStars() <= 4).mapToDouble(n -> n.getPriceAvg().doubleValue()).average().orElse(Double.NaN));

        sortedHotels = hotels.stream().filter(n -> n.getPriceFrom().compareTo(getCheapMaxAveragePrice(averageHotelPriceStandard)) > 0 &&
                n.getPriceAvg().compareTo(getLuxuryMinAveragePrice(averageHotelPriceStandard)) <= 0).sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());

        Collections.shuffle(sortedEntertainments);
        shufledEntertainment = sortedEntertainments.stream().limit(sortedEntertainments.size()/3).collect(Collectors.toList());

        Trip standardTrip = Trip.Builder.builder()
                .setTripClass(TripClass.STANDARD)
                .setFlight(flight)
                .setHotels(sortedHotels.isEmpty() ? Collections.singletonList(hotels.get(0)) : sortedHotels)
                .setEntertainment(shufledEntertainment)
                .setAveragePrice(averageFlightPriceStandard.add(averageHotelPriceStandard))
                .setDestination(flight.getDestination())
                .setPicture(String.format(PICTURE_URL, flight.getDestination()))
                .build();

        //LUXURY

        BigDecimal averageFlightPriceLuxury = new BigDecimal(flights.stream().mapToDouble(n -> n.getValue().doubleValue()).average().orElse(Double.NaN));

        sortedFlights = flights.stream().filter(FlightDirection::getDirect).sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());

        flight = sortedFlights.isEmpty() ? flights.get(flights.size() - 1) : sortedFlights.get(new Random().nextInt(sortedFlights.size()));

        hotels = hotellookFlowManager.getBasicHotels(flight.getDestination(), flight.getDepart_date(), flight.getReturn_date());
        if(CollectionUtils.isEmpty(hotels)){
            throw new RuntimeException("Any hotel offers have not been found");
        }

        BigDecimal averageHotelPriceLuxury = new BigDecimal(hotels.stream().filter(m -> m.getStars() == 5).mapToDouble(n -> n.getPriceAvg().doubleValue()).average().orElse(Double.NaN));

        sortedHotels = hotels.stream().filter(n -> n.getPriceAvg().compareTo(getLuxuryMinAveragePrice(averageHotelPriceLuxury)) > 0).sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());

        Collections.shuffle(sortedEntertainments);
        shufledEntertainment = sortedEntertainments.stream().limit(sortedEntertainments.size()/3).collect(Collectors.toList());

        Trip luxuryTrip = Trip.Builder.builder()
                .setTripClass(TripClass.LUXURY)
                .setFlight(flight)
                .setHotels(sortedHotels.isEmpty() ? Collections.singletonList(hotels.get(0)) : sortedHotels)
                .setEntertainment(shufledEntertainment)
                .setAveragePrice(averageFlightPriceLuxury.add(averageHotelPriceLuxury))
                .setDestination(flight.getDestination())
                .setPicture(String.format(PICTURE_URL, flight.getDestination()))
                .build();

        BasicTrips result = new BasicTrips();
        result.setCheapTrip(cheapTrip);
        result.setStandardTrip(standardTrip);
        result.setLuxuryTrip(luxuryTrip);
        result.setPicture(String.format(PICTURE_URL, request.getDestination()));

        return result;
    }

    //CITY

    public List<Trip> getBasicTripsByCountry(User user, GetTripRequest request){

        List<Trip> result = new ArrayList<>();

        List<FlightDirection> flights = aviasalesFlowManager.getBasicDirectionParameters(request.getSource(), request.getDestination()).stream().sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(flights)){
            throw new RuntimeException("Any airplane offers have not been found");
        }

        List<FlightDirection> uniqueSortedFlights = flights.stream().distinct().sorted(Comparator.comparing(FlightDirection::getValue)).collect(Collectors.toList());

        for(FlightDirection flight: uniqueSortedFlights){
            List<Hotel> hotels = hotellookFlowManager.getBasicHotels(flight.getDestination(), flight.getDepart_date(), flight.getReturn_date()).stream().sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(hotels)){
                continue;
            }
            List<Hotel> sortedHotels = hotels.stream().sorted(Comparator.comparing(Hotel::getPriceFrom)).collect(Collectors.toList());
            List<Entertainment> sortedEntertainments = tripsterFlowManager.getEntertainments(user, flight.getDestination(), null, null, null, null);
            List<Entertainment> shufledEntertainment = sortedEntertainments.stream().limit(sortedEntertainments.size()/3).collect(Collectors.toList());

            BigDecimal averageFlightPrice = new BigDecimal(flights.stream().mapToDouble(n -> n.getValue().doubleValue()).average().orElse(Double.NaN));
            BigDecimal averageHotelPrice = new BigDecimal(hotels.stream().mapToDouble(n -> n.getPriceAvg().doubleValue()).average().orElse(Double.NaN));

            Trip trip = Trip.Builder.builder()
                    .setFlight(flight)
                    .setHotels(sortedHotels.isEmpty() ? Collections.singletonList(hotels.get(0)) : sortedHotels)
                    .setEntertainment(Collections.unmodifiableList(shufledEntertainment))
                    .setAveragePrice(averageFlightPrice.add(averageHotelPrice))
                    .setDestination(flight.getDestination())
                    .setPicture(String.format(PICTURE_URL, flight.getDestination()))
                    .build();
            result.add(trip);
        }
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
