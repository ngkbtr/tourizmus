package ngkbtr.flowmanager;

import ngkbtr.common.auth.AuthHelper;
import ngkbtr.common.auth.model.Token;
import ngkbtr.controller.request.AuthTokenResponse;
import ngkbtr.controller.request.UserRegistrationRequest;
import ngkbtr.model.User;
import ngkbtr.model.auth.SecurityInfo;
import ngkbtr.model.user.UserInfo;
import ngkbtr.services.ValidationService;
import ngkbtr.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserRegistrationFlowManager {

    private final IStorageService storageService;
    private final ValidationService validationService;

    @Autowired
    public UserRegistrationFlowManager(IStorageService storageService, ValidationService validationService) {
        this.storageService = storageService;
        this.validationService = validationService;
    }

    public AuthTokenResponse registerUser(UserRegistrationRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        if(StringUtils.isEmpty(email)){
            throw new RuntimeException("Email must be presented");
        }
        if (validationService.checkUserAlredyExists(email)) {
            throw new RuntimeException("User with email '" + email + "' is already registred");
        }

        User user = new User(AuthHelper.createUserId(email));

        UserInfo userInfo = UserInfo.builder()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setEmail(email)
                .setPhone(request.getPhone())
                .build();

        user.setUserInfo(userInfo);

        storageService.saveUser(user);
        SecurityInfo securityInfo = new SecurityInfo(user.getUid(), request.getPassword());
        storageService.saveSecurityInfo(securityInfo);
        Token authToken = AuthHelper.createAccessToken(user.getUid());
        /*response.addCookie(new Cookie("access_token", authToken.getToken()));
        response.addCookie(new Cookie("expires_in", authToken.getExpiration()));
        response.addCookie(new Cookie("refresh_token", securityInfo.getRefreshToken()));
        return user;*/
        AuthTokenResponse authTokenResponse = new AuthTokenResponse();
        authTokenResponse.setAccessToken(authToken.getToken());
        authTokenResponse.setRefreshToken(securityInfo.getRefreshToken());
        authTokenResponse.setExpiresIn(authToken.getExpiration());

        return authTokenResponse;
    }
}