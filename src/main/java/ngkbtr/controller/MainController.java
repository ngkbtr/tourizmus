package ngkbtr.controller;

import ngkbtr.controller.request.GetCityAutocompleteRequest;
import ngkbtr.controller.request.GetFlightsRequest;
import ngkbtr.controller.request.GetHotelsRequest;
import ngkbtr.model.User;
import ngkbtr.model.auth.AuthUser;
import ngkbtr.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private final ApplicationService applicationService;

    @Autowired
    public MainController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    @ResponseBody
    public Object account(@AuthUser User user){
        return user;
    }

    //FLIGHTS

    @RequestMapping(value = "/get-flights", method = RequestMethod.POST)
    @ResponseBody
    public Object getFlights(@AuthUser User user, @RequestBody GetFlightsRequest request){
        return applicationService.getFlights(user, request);
    }

    @RequestMapping(value = "/city-autocomplete", method = RequestMethod.POST)
    @ResponseBody
    public Object getCityAutocomplete(@AuthUser User user, @RequestBody GetCityAutocompleteRequest request){
        return applicationService.getCityAutocomplete(user, request);
    }

    //HOTELS

    @RequestMapping(value = "/get-hotels", method = RequestMethod.POST)
    @ResponseBody
    public Object getHotels(@AuthUser User user, @RequestBody GetHotelsRequest request){
        return applicationService.getHotels(user, request);
    }

}
