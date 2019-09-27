package ngkbtr.controller;

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

}
