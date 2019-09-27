package ngkbtr.flowmanager;

import ngkbtr.common.auth.AuthHelper;
import ngkbtr.common.auth.model.AuthPayload;
import ngkbtr.common.auth.model.Token;
import ngkbtr.model.auth.SecurityInfo;
import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.model.exception.TokenExpiredException;
import ngkbtr.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static ngkbtr.common.auth.AuthHelper.validateRefreshToken;

@Service
public class AuthenticationTokenFlowManager {

    private final IStorageService storageService;

    @Autowired
    public AuthenticationTokenFlowManager(IStorageService storageService) {
        this.storageService = storageService;
    }

    public void getNewAccessToken(String refreshToken, HttpServletResponse response) throws TokenExpiredException, FailedAuthenticationException {
        if (StringUtils.hasText(refreshToken)) {
            validateRefreshToken(refreshToken);
            AuthPayload payload = AuthHelper.parseTokenPayload(refreshToken);
            SecurityInfo securityInfo = storageService.getSecurityInfoById(payload.getUid());
            if (securityInfo.isRefreshTokenExpired()) {
                throw new TokenExpiredException();
            }
            Token newAuthToken = AuthHelper.createAccessToken(securityInfo.getUid());
            Token newRefreshToken = securityInfo.updateRefreshToken(securityInfo.getUid());
            storageService.saveSecurityInfo(securityInfo);
            response.addCookie(new Cookie("access_token", newAuthToken.getToken()));
            response.addCookie(new Cookie("expires_in", newAuthToken.getExpiration()));
            response.addCookie(new Cookie("refresh_token", newRefreshToken.getToken()));
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }
}
