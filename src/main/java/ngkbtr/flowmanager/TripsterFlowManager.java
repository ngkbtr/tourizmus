package ngkbtr.flowmanager;

import com.google.gson.Gson;
import ngkbtr.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TripsterFlowManager {

    private static final String AUTH_TOKEN = "9b3257e04e9d4305d76066ef5bd32d9d6e4dec3f";

    private static final String API_URL = "https://experience.tripster.ru/api/experiences/?city__iata=%s";

    public List<Entertainment> getEntertainments(User user, String location, BigDecimal maxPrice, Double minRating, Boolean personal, Long count){
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = String.format(API_URL, location);

        EntertainmentsCnt entertainmentCnt = template.exchange(url, HttpMethod.GET, entity, EntertainmentsCnt.class).getBody();
        System.out.println(new Gson().toJson(entertainmentCnt));
        List<Entertainment> result = Arrays.stream(entertainmentCnt.getResults()).collect(Collectors.toList());
        while(StringUtils.hasText(entertainmentCnt.getNext())){
            result.addAll(Arrays.asList(entertainmentCnt.getResults()));
            entertainmentCnt = template.exchange(entertainmentCnt.getNext(), HttpMethod.GET, entity, EntertainmentsCnt.class).getBody();
            System.out.println(new Gson().toJson(entertainmentCnt));
        }

        result = result.stream().filter(n -> "active".equals(n.getStatus())).collect(Collectors.toList());
        Collections.shuffle(result);

        if(maxPrice != null){
            result = result.stream().filter(n -> maxPrice.compareTo(n.getPrice().getValue()) >= 0).collect(Collectors.toList());
        }
        if(minRating != null){
            result = result.stream().filter(n -> Objects.equals(n.getRating(), minRating)).collect(Collectors.toList());
        }
        if(personal != null){
            result = result.stream().filter(n -> "private".equals(n.getType())).collect(Collectors.toList());
        }

        return count != null ? result.stream().limit(count).collect(Collectors.toList()) : result;
    }
}
