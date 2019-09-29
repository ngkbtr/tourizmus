package ngkbtr.services;

import ngkbtr.controller.request.*;
import ngkbtr.flowmanager.*;
import ngkbtr.model.User;
import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.model.exception.TokenExpiredException;
import ngkbtr.flowmanager.CityAutocompleteObject;
import ngkbtr.flowmanager.FlightDirection;
import ngkbtr.model.trip.BasicTrips;
import ngkbtr.model.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

@Service
public class ApplicationService {
    private final UserRegistrationFlowManager registrationFlowManager;
    private final UserAuthFlowManager authFlowManager;
    private final AuthenticationTokenFlowManager authenticationTokenFlowManager;
    private final VKUserAuthFlowManager vkUserAuthFlowManager;

    private final AviasalesFlowManager aviasalesFlowManager;
    private final HotellookFlowManager hotellookFlowManager;
    private final TripsterFlowManager tripsterFlowManager;
    private final TripFlowManager tripFlowManager;

    @Autowired
    public ApplicationService(UserRegistrationFlowManager registrationFlowManager, UserAuthFlowManager authFlowManager, AuthenticationTokenFlowManager authenticationTokenFlowManager, VKUserAuthFlowManager vkUserAuthFlowManager, AviasalesFlowManager aviasalesFlowManager, HotellookFlowManager hotellookFlowManager, TripsterFlowManager tripsterFlowManager, TripFlowManager tripFlowManager) {
        this.registrationFlowManager = registrationFlowManager;
        this.authFlowManager = authFlowManager;
        this.authenticationTokenFlowManager = authenticationTokenFlowManager;
        this.vkUserAuthFlowManager = vkUserAuthFlowManager;
        this.aviasalesFlowManager = aviasalesFlowManager;
        this.hotellookFlowManager = hotellookFlowManager;
        this.tripsterFlowManager = tripsterFlowManager;
        this.tripFlowManager = tripFlowManager;
    }

    public AuthTokenResponse getNewAccessToken(String refreshToken, HttpServletResponse response) throws FailedAuthenticationException, TokenExpiredException {
        return authenticationTokenFlowManager.getNewAccessToken(refreshToken, response);
    }

    public AuthTokenResponse registerUser(UserRegistrationRequest request, HttpServletResponse response){
        return registrationFlowManager.registerUser(request, response);
    }

    public AuthTokenResponse authUser(UserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
        return authFlowManager.authUser(request, response);
    }

    public AuthTokenResponse authVKUser(VKUserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
        return vkUserAuthFlowManager.authVKUser(request, response);
    }

    ////

    public List<FlightDirection> getFlights(User user, GetFlightsRequest request){
        return aviasalesFlowManager.getDirectionParameters(user, request);
    }

    public Set<CityAutocompleteObject> getCityAutocomplete(User user, GetCityAutocompleteRequest request){
        return aviasalesFlowManager.getCityAutocomplete(user, request);
    }

    public List<Hotel> getHotels(User user, GetHotelsRequest request){
        return hotellookFlowManager.getHotels(user, request);
    }

    public List<Trip> getTripsByCountry(User user, GetTripRequest request){
        return tripFlowManager.getBasicTripsByCountry(user, request);
    }

    public BasicTrips getTripsByCity(User user, GetTripRequest request){
        return tripFlowManager.getBasicTripsByCity(user, request);
    }

    public List<Entertainment> getEntertainments(User user, GetEntertainmentRequest request){
        return tripsterFlowManager.getEntertainments(user, request.getLocation(), null, null, null, 20L);
    }

    public RedirectUrlResponse redirectToBuyTicket(User user, RedirectUrlRequest request){
        return aviasalesFlowManager.redirectUserToBuyTicket(user, request);
    }
}
