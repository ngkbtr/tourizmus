package ngkbtr.flowmanager;

import com.google.gson.Gson;
import ngkbtr.controller.request.GetCityAutocompleteRequest;
import ngkbtr.controller.request.GetFlightsRequest;
import ngkbtr.controller.request.RedirectUrlRequest;
import ngkbtr.controller.request.RedirectUrlResponse;
import ngkbtr.model.User;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AviasalesFlowManager {

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER_HOLDER = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    private static final Gson GSON = new Gson();

    private static final String GET_FLIGHTS_API_URL = "https://lyssa.aviasales.ru/v2/widget/month/?origin_iata=%s&destination_iata=%s&one_way=%s&min_trip_duration=%s&max_trip_duration=%s&depart_month=%s";
    private static final String GET_CITY_AUTOCOMPLETE_URL = "http://autocomplete.travelpayouts.com/places2?term=%s&locale=ru";
    private static final String REDIRECT_TO_BUY_URL = "https://www.aviasales.ru/search/%s%s%s%s%s%s1?ticket=%s";
    private static final String AUTH_TOKEN = "5c14f2fc70f4e1a246019c8080a225a1";

    public Set<CityAutocompleteObject> getCityAutocomplete(User user, GetCityAutocompleteRequest request){

        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<CityAutocompleteObject[]> responseEntity = template.exchange(String.format(GET_CITY_AUTOCOMPLETE_URL, request.getTerm()), HttpMethod.GET, entity, CityAutocompleteObject[].class);
        return Arrays.stream(responseEntity.getBody()).filter(n -> StringUtils.hasText(n.getCityName())).collect(Collectors.toSet());
    }

    public List<FlightDirection> getDirectionParameters(User user, GetFlightsRequest request){

        List<FlightDirection> result = new ArrayList<>();

        RestTemplate template = new RestTemplate();
        String url = String.format(GET_FLIGHTS_API_URL, request.getSource(),
                request.getDestination(),
                request.getOneWay() != null ? request.getOneWay().toString() : "false",
                "1",
                request.getDuration(),
                getFirstDayOfMonth(request.getStartDate()));

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        headers.add("X-Access-Token", AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.GET, entity, String.class);

        JSONObject json = new JSONObject(responseEntity.getBody());

        if(json.has("month")) {
            JSONObject monthJson = json.getJSONObject("month");
            Iterator<String> iter = monthJson.keys();
            while (iter.hasNext()){
                result.add(GSON.fromJson(monthJson.getJSONObject(iter.next()).toString(), FlightDirection.class));
            }
        } else {
            throw new RuntimeException("Request has not been successfull");
        }

        if(isCrossMonth(request.getStartDate(), request.getDuration())){
            url = String.format(GET_FLIGHTS_API_URL, request.getSource(),
                    request.getDestination(),
                    request.getOneWay() != null ? request.getOneWay().toString() : "false",
                    "1",
                    getDurationIfNextMonth(request.getStartDate(), request.getDuration()),
                    getFirstDayOfNextMonth(request.getStartDate(), request.getDuration()));

            System.out.println(url);

            headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            headers.add("X-Access-Token", AUTH_TOKEN);
            entity = new HttpEntity<>("parameters", headers);
            responseEntity = template.exchange(url, HttpMethod.GET, entity, String.class);

            json = new JSONObject(responseEntity.getBody());
            if(json.has("month")) {
                JSONObject monthJson = json.getJSONObject("month");
                Iterator<String> iter = monthJson.keys();
                while (iter.hasNext()){
                    result.add(GSON.fromJson(monthJson.getJSONObject(iter.next()).toString(), FlightDirection.class));
                }
            } else {
                throw new RuntimeException("Request has not been successfull");
            }
        }

        return result;
    }

    public List<FlightDirection> getBasicDirectionParameters(String source, String destination){
        RestTemplate template = new RestTemplate();
        String startDate = DATE_FORMATTER_HOLDER.get().format(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));

        String url = String.format(GET_FLIGHTS_API_URL, source,
                destination,
                "false",
                "1",
                "30",
                getFirstDayOfMonth(startDate));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        headers.add("X-Access-Token", AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.GET, entity, String.class);

        JSONObject json = new JSONObject(responseEntity.getBody());

        List<FlightDirection> result = new ArrayList<>();

        if(json.has("month")) {
            JSONObject monthJson = json.getJSONObject("month");
            Iterator<String> iter = monthJson.keys();
            while (iter.hasNext()){
                result.add(GSON.fromJson(monthJson.getJSONObject(iter.next()).toString(), FlightDirection.class));
            }
        } else {
            throw new RuntimeException("Request has not been successfull");
        }
        if(isCrossMonth(startDate, "30")){
            url = String.format(GET_FLIGHTS_API_URL, source,
                    destination,
                    false,
                    "1",
                    getDurationIfNextMonth(startDate, "30"),
                    getFirstDayOfNextMonth(startDate, "30"));

            System.out.println(url);

            headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            headers.add("X-Access-Token", AUTH_TOKEN);
            entity = new HttpEntity<>("parameters", headers);
            responseEntity = template.exchange(url, HttpMethod.GET, entity, String.class);

            json = new JSONObject(responseEntity.getBody());
            if(json.has("month")) {
                JSONObject monthJson = json.getJSONObject("month");
                Iterator<String> iter = monthJson.keys();
                while (iter.hasNext()){
                    result.add(GSON.fromJson(monthJson.getJSONObject(iter.next()).toString(), FlightDirection.class));
                }
            } else {
                throw new RuntimeException("Request has not been successfull");
            }
        }
        return result;
    }

    public RedirectUrlResponse redirectUserToBuyTicket(User user, RedirectUrlRequest request){
        //<origin_iata> <depart_day> <depart_month> <destination_iata> <return_day> <return_month> 1?ticket=<signature>
        FlightDirection direction = request.getFlight();
        return new RedirectUrlResponse(String.format(REDIRECT_TO_BUY_URL, direction.getOrigin(),
                getDay(direction.getDepart_date()),
                getMonth(direction.getDepart_date()),
                direction.getDestination(),
                getDay(direction.getReturn_date()),
                getMonth(direction.getReturn_date()),
                direction.getSignature()));
    }

    private static String getDay(String date){
        return date.split("-")[2];
    }

    private static String getMonth(String date){
        return date.split("-")[1];
    }

    private static String getFirstDayOfMonth(String startDay){
        return startDay.substring(0, startDay.lastIndexOf("-")).concat("-01");
    }

    private static boolean isCrossMonth(String startDate, String duration){
        Date start = null;
        try {
            start = DATE_FORMATTER_HOLDER.get().parse(startDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date end = new Date(start.getTime() + TimeUnit.DAYS.toMillis(Long.parseLong(duration)));
        return !(DATE_FORMATTER_HOLDER.get().format(start).split("-")[1]).equals(DATE_FORMATTER_HOLDER.get().format(end).split("-")[1]);
    }

    private static String getDurationIfNextMonth(String startDate, String duration){
        Date start = null;
        try {
            start = DATE_FORMATTER_HOLDER.get().parse(startDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date end = new Date(start.getTime() + TimeUnit.DAYS.toMillis(Long.parseLong(duration)));
        String days = DATE_FORMATTER_HOLDER.get().format(end).split("-")[2];
        if(days.startsWith("0")){
            days = days.replaceFirst("0", "");
        }
        return days;
    }

    private static String getFirstDayOfNextMonth(String startDate, String duration){
        Date start = null;
        try {
            start = DATE_FORMATTER_HOLDER.get().parse(startDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date end = new Date(start.getTime() + TimeUnit.DAYS.toMillis(Long.parseLong(duration)));
        return getFirstDayOfMonth(DATE_FORMATTER_HOLDER.get().format(end));
    }
}
