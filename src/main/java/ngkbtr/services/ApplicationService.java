package ngkbtr.services;

import ngkbtr.controller.request.UserAuthRequest;
import ngkbtr.controller.request.UserRegistrationRequest;
import ngkbtr.controller.request.VKUserAuthRequest;
import ngkbtr.flowmanager.AuthenticationTokenFlowManager;
import ngkbtr.flowmanager.UserAuthFlowManager;
import ngkbtr.flowmanager.UserRegistrationFlowManager;
import ngkbtr.flowmanager.VKUserAuthFlowManager;
import ngkbtr.model.User;
import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.model.exception.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.util.List;

@Service
public class ApplicationService {
    private final UserRegistrationFlowManager registrationFlowManager;
    private final UserAuthFlowManager authFlowManager;
    private final AuthenticationTokenFlowManager authenticationTokenFlowManager;
    private final VKUserAuthFlowManager vkUserAuthFlowManager;

    @Autowired
    public ApplicationService(UserRegistrationFlowManager registrationFlowManager, UserAuthFlowManager authFlowManager, AuthenticationTokenFlowManager authenticationTokenFlowManager, VKUserAuthFlowManager vkUserAuthFlowManager) {
        this.registrationFlowManager = registrationFlowManager;
        this.authFlowManager = authFlowManager;
        this.authenticationTokenFlowManager = authenticationTokenFlowManager;
        this.vkUserAuthFlowManager = vkUserAuthFlowManager;
    }

    public void getNewAccessToken(String refreshToken, HttpServletResponse response) throws FailedAuthenticationException, TokenExpiredException {
        authenticationTokenFlowManager.getNewAccessToken(refreshToken, response);
    }

    public User registerUser(UserRegistrationRequest request, HttpServletResponse response) throws ValidationException {
        return registrationFlowManager.registerUser(request, response);
    }

    public User authUser(UserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
        return authFlowManager.authUser(request, response);
    }

    public User authVKUser(VKUserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
        return vkUserAuthFlowManager.authVKUser(request, response);
    }
}
