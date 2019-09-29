package ngkbtr.flowmanager;

import ngkbtr.common.auth.AuthHelper;
import ngkbtr.common.auth.model.Token;
import ngkbtr.controller.request.AuthTokenResponse;
import ngkbtr.controller.request.UserAuthRequest;
import ngkbtr.model.User;
import ngkbtr.model.auth.SecurityInfo;
import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.services.ValidationService;
import ngkbtr.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserAuthFlowManager {

    private final IStorageService storageService;
    private final ValidationService validationService;

    @Autowired
    public UserAuthFlowManager(IStorageService storageService, ValidationService validationService) {
        this.storageService = storageService;
        this.validationService = validationService;
    }

    public AuthTokenResponse authUser(UserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
        User user = storageService.getUserById(AuthHelper.createUserId(request.getEmail()));
        if(user != null){
            SecurityInfo securityInfo = storageService.getSecurityInfoById(user.getUid());
            if(validationService.checkUserPassword(securityInfo, request.getPassword())){
                Token authToken = AuthHelper.createAccessToken(securityInfo.getUid());
                Token refreshToken = securityInfo.updateRefreshToken(securityInfo.getUid());
                storageService.saveSecurityInfo(securityInfo);
                /*response.addCookie(new Cookie("access_token", authToken.getToken()));
                response.addCookie(new Cookie("expires_in", authToken.getExpiration()));
                response.addCookie(new Cookie("refresh_token", refreshToken.getToken()));
                return user;*/
                AuthTokenResponse authTokenResponse = new AuthTokenResponse();
                authTokenResponse.setAccessToken(authToken.getToken());
                authTokenResponse.setRefreshToken(refreshToken.getToken());
                authTokenResponse.setExpiresIn(authToken.getExpiration());

                return authTokenResponse;
            } else {
                throw new FailedAuthenticationException("Password is incorrect");
            }
        } else {
            throw new FailedAuthenticationException("User not registered");
        }
    }
}
