package ngkbtr.flowmanager;

import ngkbtr.controller.request.GetHotelsRequest;
import ngkbtr.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class HotellookFlowManager {

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER_HOLDER = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    private static final String HOTELS_API_URL = "http://engine.hotellook.com/api/v2/cache.json?location=%s&currency=rub&checkIn=%s&checkOut=%s&limit=100";

    public List<Hotel> getHotels(User user, GetHotelsRequest request) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = String.format(HOTELS_API_URL, request.getLocation(), request.getStartDate(), request.getEndDate());
        ResponseEntity<Hotel[]> responseEntity = template.exchange(url, HttpMethod.GET, entity, Hotel[].class);

        List<Hotel> result = Arrays.stream(responseEntity.getBody()).collect(Collectors.toList());

        try {
            final BigDecimal divider = new BigDecimal(TimeUnit.MILLISECONDS.toDays((DATE_FORMATTER_HOLDER.get().parse(request.getEndDate()).getTime()) - (DATE_FORMATTER_HOLDER.get().parse(request.getStartDate()).getTime())));

            if (request.getMaxPricePerNight() != null) {
                result = result.stream().filter(n -> {
                    BigDecimal maxPricePerNight = n.getPriceAvg().divide(divider);
                    return maxPricePerNight.compareTo(request.getMaxPricePerNight()) <= 0;
                }).collect(Collectors.toList());
            }
            if (request.getMinPricePerNight() != null) {
                result = result.stream().filter(n -> {
                    BigDecimal minPricePerNight = n.getPriceAvg().divide(divider);
                    return minPricePerNight.compareTo(request.getMinPricePerNight()) > 0;
                }).collect(Collectors.toList());
            }
            if (request.getStars() != null) {
                Long stars = request.getStars();
                if (stars < 0 || stars > 5) {
                    throw new RuntimeException("Stars must be between 0 and 5");
                }
                result = result.stream().filter(n -> Objects.equals(stars, n.getStars())).collect(Collectors.toList());
            }
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public List<Hotel> getBasicHotels(String location, String startDate, String endDate) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = String.format(HOTELS_API_URL, location, startDate, endDate);
        ResponseEntity<Hotel[]> responseEntity = template.exchange(url, HttpMethod.GET, entity, Hotel[].class);
        return Arrays.stream(responseEntity.getBody()).collect(Collectors.toList());
    }
}

