package ngkbtr.controller;

import ngkbtr.controller.request.UserAuthRequest;
import ngkbtr.controller.request.UserRegistrationRequest;
import ngkbtr.controller.request.VKUserAuthRequest;
import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.model.exception.TokenExpiredException;
import ngkbtr.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;

@Controller
public class AuthController {

    private final ApplicationService applicationService;

    @Autowired
    public AuthController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RequestMapping(value = "/auth-vk", method = RequestMethod.POST)
    @ResponseBody
    public Object auth(@RequestBody VKUserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
        return applicationService.authVKUser(request, response);
    }

    @RequestMapping(value = "/refresh-token", method = RequestMethod.GET)
    @ResponseBody
    public void refreshToken(@RequestHeader("Bearer") String refreshToken, HttpServletResponse response) throws FailedAuthenticationException, TokenExpiredException {
        applicationService.getNewAccessToken(refreshToken, response);
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseBody
    public Object signUp(@RequestBody UserRegistrationRequest request, HttpServletResponse response) throws ValidationException {
        return applicationService.registerUser(request, response);
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    @ResponseBody
    public Object signIn(@RequestBody UserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
        return applicationService.authUser(request, response);
    }
}
