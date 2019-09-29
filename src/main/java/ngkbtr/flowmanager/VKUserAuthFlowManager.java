package ngkbtr.flowmanager;

import com.google.gson.Gson;
import ngkbtr.common.auth.AuthHelper;
import ngkbtr.common.auth.model.Token;
import ngkbtr.controller.request.AuthTokenResponse;
import ngkbtr.controller.request.VKUserAuthRequest;
import ngkbtr.model.User;
import ngkbtr.model.auth.SecurityInfo;
import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.model.user.UserInfo;
import ngkbtr.services.ValidationService;
import ngkbtr.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class VKUserAuthFlowManager {

    private static final Gson GSON = new Gson();

    private final IStorageService storageService;
    private final ValidationService validationService;

    @Autowired
    public VKUserAuthFlowManager(IStorageService storageService, ValidationService validationService) {
        this.storageService = storageService;
        this.validationService = validationService;
    }

    public AuthTokenResponse authVKUser(VKUserAuthRequest request, HttpServletResponse response) throws FailedAuthenticationException {
        if(validationService.validateVkAuthUser(request)) {
            String uid = AuthHelper.createUserId(request.getSession().getUser().getId());
            User user = storageService.getUserById(uid);
            if (user != null) {
                SecurityInfo securityInfo = storageService.getSecurityInfoById(user.getUid());
                Token authToken = AuthHelper.createAccessToken(securityInfo.getUid());
                Token refreshToken = securityInfo.updateRefreshToken(securityInfo.getUid());
                storageService.saveSecurityInfo(securityInfo);
                /*response.addCookie(new Cookie("access_token", authToken.getToken()));
                response.addCookie(new Cookie("expires_in", authToken.getExpiration()));
                response.addCookie(new Cookie("refresh_token", refreshToken.getToken()));*/

                AuthTokenResponse authTokenResponse = new AuthTokenResponse();
                authTokenResponse.setAccessToken(authToken.getToken());
                authTokenResponse.setRefreshToken(refreshToken.getToken());
                authTokenResponse.setExpiresIn(authToken.getExpiration());

                return authTokenResponse;
            } else {
                user = new User(uid);
                UserInfo userInfo = UserInfo.builder()
                        .setFirstName(request.getSession().getUser().getFirstName())
                        .setLastName(request.getSession().getUser().getLastName())
                        .build();

                user.setUserInfo(userInfo);
                storageService.saveUser(user);
                SecurityInfo securityInfo = new SecurityInfo(user.getUid(), null);
                storageService.saveSecurityInfo(securityInfo);
                Token authToken = AuthHelper.createAccessToken(user.getUid());
                /*response.addCookie(new Cookie("access_token", authToken.getToken()));
                response.addCookie(new Cookie("expires_in", authToken.getExpiration()));
                response.addCookie(new Cookie("refresh_token", securityInfo.getRefreshToken()));*/
                AuthTokenResponse authTokenResponse = new AuthTokenResponse();
                authTokenResponse.setAccessToken(authToken.getToken());
                authTokenResponse.setRefreshToken(securityInfo.getRefreshToken());
                authTokenResponse.setExpiresIn(authToken.getExpiration());

                return authTokenResponse;
            }
        } else {
            throw new FailedAuthenticationException();
        }
    }
}
