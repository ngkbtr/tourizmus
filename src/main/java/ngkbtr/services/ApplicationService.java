package ngkbtr.services;

import ngkbtr.controller.request.*;
import ngkbtr.flowmanager.*;
import ngkbtr.model.User;
import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.model.exception.TokenExpiredException;
import ngkbtr.flowmanager.CityAutocompleteObject;
import ngkbtr.flowmanager.FlightDirection;
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

    @Autowired
    public ApplicationService(UserRegistrationFlowManager registrationFlowManager, UserAuthFlowManager authFlowManager, AuthenticationTokenFlowManager authenticationTokenFlowManager, VKUserAuthFlowManager vkUserAuthFlowManager, AviasalesFlowManager aviasalesFlowManager, HotellookFlowManager hotellookFlowManager) {
        this.registrationFlowManager = registrationFlowManager;
        this.authFlowManager = authFlowManager;
        this.authenticationTokenFlowManager = authenticationTokenFlowManager;
        this.vkUserAuthFlowManager = vkUserAuthFlowManager;
        this.aviasalesFlowManager = aviasalesFlowManager;
        this.hotellookFlowManager = hotellookFlowManager;
    }

    public void getNewAccessToken(String refreshToken, HttpServletResponse response) throws FailedAuthenticationException, TokenExpiredException {
        authenticationTokenFlowManager.getNewAccessToken(refreshToken, response);
    }

    public User registerUser(UserRegistrationRequest request, HttpServletResponse response){
        return registrationFlowManager.registerUser(request, response);
    }

    public User authUser(UserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
        return authFlowManager.authUser(request, response);
    }

    public User authVKUser(VKUserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
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
}
